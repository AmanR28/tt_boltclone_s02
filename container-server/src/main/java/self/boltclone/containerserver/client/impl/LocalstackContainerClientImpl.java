package self.boltclone.containerserver.client.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import self.boltClone.enums.ContainerStatus;
import self.boltclone.containerserver.client.ContainerClient;
import self.boltclone.containerserver.dto.ContainerDto;
import software.amazon.awssdk.services.ecs.EcsClient;
import software.amazon.awssdk.services.ecs.model.*;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class LocalstackContainerClientImpl implements ContainerClient {

    @Autowired
    private EcsClient ecsClient;

    @Value("${env.aws.ecs.clusterId}")
    private String clusterId;

    @Value("${env.aws.ecs.taskId}")
    private String taskId;

    @Value("${env.aws.ecs.subnetId}")
    private String subnetId;

    @Value("${env.container.port}")
    private int containerPort;

    @Override
    public ContainerDto create(String projectId) {

        createService(projectId);

        Task task = createTask(clusterId, projectId);

        Container container = task.containers().get(0);
        String containerArn = container.containerArn();
        String containerName = container.name();
        String containerId = containerArn.substring(containerArn.lastIndexOf('/') + 1);

        NetworkBinding containerNetwork = container.networkBindings().stream()
            .filter(nb -> nb.containerPort() == containerPort)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Port not found"));

        return ContainerDto.builder()
            .projectId(projectId)
            .id(containerId)
            .name(containerName)
            .url(containerNetwork.bindIP() + ":" + containerNetwork.hostPort())
            .status(ContainerStatus.NEW)
            .build();
    }

    @Override
    public void update(String projectId, String containerName, String gitPatch) {

        if (!serviceExists(projectId)) {
            throw new RuntimeException("Service not found: " + projectId +
                ". Please create the service first.");
        }

        String encodedPatch =
            Base64.getEncoder().encodeToString(gitPatch.getBytes(StandardCharsets.UTF_8));
        List<String> commandList = Arrays.asList(
            "sh",
            "-c",
            "echo " + encodedPatch
                + " | base64 -d > /app/current.patch && git apply /app/current.patch && pm2-runtime start npm -- run dev"
        );

        String updateTaskDefinition = updateTaskId(containerName, projectId, commandList);

        UpdateServiceRequest updateServiceRequest = UpdateServiceRequest.builder()
            .cluster(clusterId)
            .service(projectId)
            .taskDefinition(updateTaskDefinition)
            .forceNewDeployment(true)
            .build();

        ecsClient.updateService(updateServiceRequest);
    }

    private void createService(String serviceName) {

        CreateServiceRequest createServiceRequest = CreateServiceRequest.builder()
            .cluster(clusterId)
            .serviceName(serviceName)
            .taskDefinition(taskId)
            .desiredCount(1)
            .launchType(LaunchType.FARGATE)
            .networkConfiguration(NetworkConfiguration.builder()
                .awsvpcConfiguration(AwsVpcConfiguration.builder()
                    .subnets(subnetId)
                    .assignPublicIp(AssignPublicIp.ENABLED)
                    .build())
                .build())
            .build();

        ecsClient.createService(createServiceRequest);
    }

    private boolean serviceExists(String serviceName) {

        try {
            DescribeServicesRequest request = DescribeServicesRequest.builder()
                .cluster(clusterId)
                .services(serviceName)
                .build();

            DescribeServicesResponse response = ecsClient.describeServices(request);

            return !response.services().isEmpty() &&
                !"INACTIVE".equals(response.services().get(0).status());

        } catch (Exception e) {
            return false;
        }
    }

    private Task createTask(String clusterId, String serviceName) {

        int maxAttempts = 30;
        int attempts = 0;

        while (attempts < maxAttempts) {
            try {
                ListTasksRequest listTasksRequest = ListTasksRequest.builder()
                    .cluster(clusterId)
                    .serviceName(serviceName)
                    .desiredStatus(DesiredStatus.RUNNING)
                    .build();

                ListTasksResponse listTasksResponse = ecsClient.listTasks(listTasksRequest);

                if (!listTasksResponse.taskArns().isEmpty()) {
                    DescribeTasksRequest describeRequest = DescribeTasksRequest.builder()
                        .cluster(clusterId)
                        .tasks(listTasksResponse.taskArns().get(0))
                        .build();

                    DescribeTasksResponse describeResponse =
                        ecsClient.describeTasks(describeRequest);
                    Task task = describeResponse.tasks().get(0);

                    if ("RUNNING".equals(task.lastStatus())) {
                        return task;
                    }
                }

                Thread.sleep(10000);
                attempts++;
            } catch (Exception e) {
                attempts++;
            }
        }

        throw new RuntimeException("Service task did not start within timeout period");
    }

    private String updateTaskId(String containerName, String serviceName,
        List<String> commandList) {

        RegisterTaskDefinitionRequest registerRequest =
            updateTaskDefinition(containerName, serviceName, commandList);
        RegisterTaskDefinitionResponse registerResponse =
            ecsClient.registerTaskDefinition(registerRequest);
        return registerResponse.taskDefinition().taskDefinitionArn();
    }

    private RegisterTaskDefinitionRequest updateTaskDefinition(String containerName,
        String serviceName, List<String> commandList) {

        ContainerDefinition containerDefinition = ContainerDefinition.builder()
            .name(containerName)
            .image("tt-boltclone-image:latest")
            .command(commandList)
            .portMappings(PortMapping.builder()
                .containerPort(8080)
                .hostPort(80)
                .protocol(TransportProtocol.TCP)
                .build())
            .logConfiguration(LogConfiguration.builder()
                .logDriver(LogDriver.AWSLOGS)
                .options(Map.of(
                    "awslogs-group", "/ecs/test",
                    "awslogs-region", "us-east-1",
                    "awslogs-stream-prefix", "ecs"
                ))
                .build())
            .build();

        RegisterTaskDefinitionRequest registerRequest = RegisterTaskDefinitionRequest.builder()
            .family(serviceName)
            .requiresCompatibilities(Compatibility.FARGATE)
            .networkMode(NetworkMode.AWSVPC)
            .cpu("256")
            .memory("512")
            .containerDefinitions(containerDefinition)
            .build();

        return registerRequest;
    }

}
