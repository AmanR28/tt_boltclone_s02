package self.boltclone.containerserver.client.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import self.boltClone.enums.ContainerStatus;
import self.boltclone.containerserver.client.ContainerClient;
import self.boltclone.containerserver.dto.ContainerDto;
import software.amazon.awssdk.services.ecs.EcsClient;
import software.amazon.awssdk.services.ecs.model.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Component
@Primary
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
    public ContainerDto create() {

        ContainerDto containerDto = null;
        String projectId = "1";

        try {
            // 1. Create ECS Service (replaces runTask)
            CreateServiceRequest createServiceRequest = CreateServiceRequest.builder()
                .cluster(clusterId)
                .serviceName("react-app-" + projectId)
                .taskDefinition(taskId)
                .desiredCount(1)
                .launchType(LaunchType.FARGATE)
                .networkConfiguration(
                    NetworkConfiguration.builder()
                        .awsvpcConfiguration(
                            AwsVpcConfiguration.builder()
                                .subnets(subnetId)
                                .assignPublicIp(AssignPublicIp.ENABLED)
                                .build())
                        .build())
                .build();

            CreateServiceResponse serviceResponse = ecsClient.createService(createServiceRequest);
            log.info("Service created: {}", serviceResponse.service().serviceName());

            // 2. Wait for service to have running tasks
            String serviceName = "react-app-" + projectId;
            Task task = waitForServiceTask(clusterId, serviceName);

            // 3. Extract container information (same as your existing code)
            Container container = task.containers().get(0);
            String containerArn = container.containerArn();
            String containerId = containerArn.substring(containerArn.lastIndexOf('/') + 1);
            NetworkBinding containerNetwork =
                container.networkBindings().stream()
                    .filter(nb -> nb.containerPort() == containerPort)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Port not found"));

            // 4. Build ContainerDto (same as your existing code)
            containerDto = ContainerDto.builder()
                .id(containerId)
                .url(containerNetwork.bindIP() + ":" + containerNetwork.hostPort())
                .status(ContainerStatus.NEW)
                .build();
            return containerDto;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }

        return containerDto;
    }


    private Task waitForServiceTask(String clusterId, String serviceName) {
        int maxAttempts = 30; // 5 minutes with 10-second intervals
        int attempts = 0;

        while (attempts < maxAttempts) {
            try {
                // List tasks for the service
                ListTasksRequest listTasksRequest = ListTasksRequest.builder()
                    .cluster(clusterId)
                    .serviceName(serviceName)
                    .desiredStatus(DesiredStatus.RUNNING)
                    .build();

                ListTasksResponse listTasksResponse = ecsClient.listTasks(listTasksRequest);

                if (!listTasksResponse.taskArns().isEmpty()) {
                    // Describe the first running task
                    DescribeTasksRequest describeRequest = DescribeTasksRequest.builder()
                        .cluster(clusterId)
                        .tasks(listTasksResponse.taskArns().get(0))
                        .build();

                    DescribeTasksResponse describeResponse = ecsClient.describeTasks(describeRequest);
                    Task task = describeResponse.tasks().get(0);

                    if ("RUNNING".equals(task.lastStatus())) {
                        log.info("Service task is running: {}", task.taskArn());
                        return task;
                    }
                }

                Thread.sleep(10000); // Wait 10 seconds
                attempts++;
                log.info("Waiting for service task to be running... Attempt {}/{}", attempts, maxAttempts);

            } catch (Exception e) {
                log.error("Error waiting for service task: {}", e.getMessage());
                attempts++;
            }
        }

        throw new RuntimeException("Service task did not start within timeout period");
    }

    @Override
    public void update(String containerId, String gitPatch) {

        String encodedPatch =
            Base64.getEncoder().encodeToString(gitPatch.getBytes(StandardCharsets.UTF_8));

        RunTaskResponse response = ecsClient.runTask(
            RunTaskRequest.builder()
                .cluster(clusterId)
                .launchType(LaunchType.FARGATE)
                .taskDefinition(taskId)
                .overrides(TaskOverride.builder()
                    .containerOverrides(ContainerOverride.builder()
                        .name(containerId)
                        .command("sh", "-c", String.join(" ",
                            "echo", encodedPatch, "| base64 -d > /app/current.patch",
                            "&& git apply /app/current.patch",
                            "&& pm2-runtime start npm -- run dev"
                        ))
                        .build())
                    .build())
                .networkConfiguration(
                    NetworkConfiguration.builder()
                        .awsvpcConfiguration(
                            AwsVpcConfiguration.builder()
                                .subnets(subnetId)
                                .assignPublicIp(AssignPublicIp.ENABLED)
                                .build())
                        .build())
                .build());
        log.info("Task update response: {}", response);
    }
}
