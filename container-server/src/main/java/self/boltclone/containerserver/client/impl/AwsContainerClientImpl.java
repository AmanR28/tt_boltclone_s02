package self.boltclone.containerserver.client.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import self.boltClone.enums.ContainerStatus;
import self.boltclone.containerserver.client.ContainerClient;
import self.boltclone.containerserver.dto.ContainerDto;
import software.amazon.awssdk.services.ecs.EcsClient;
import software.amazon.awssdk.services.ecs.model.*;

@Slf4j
@Component
public class AwsContainerClientImpl {
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

//    @Override
    public ContainerDto create() {

        ContainerDto containerDto = null;

        try {
            RunTaskResponse response =
                ecsClient.runTask(
                    RunTaskRequest.builder()
                        .cluster(clusterId)
                        .launchType(LaunchType.FARGATE)
                        .taskDefinition(taskId)
                        .enableExecuteCommand(true)
                        .networkConfiguration(
                            NetworkConfiguration.builder()
                                .awsvpcConfiguration(
                                    AwsVpcConfiguration.builder()
                                        .subnets(subnetId)
                                        .assignPublicIp(AssignPublicIp.ENABLED)
                                        .build())
                                .build())
                        .build());
            log.info("Task run response: {}", response);

            Task task = response.tasks().get(0);

            Container container = task.containers().get(0);
            String containerArn = container.containerArn();
            String containerId = containerArn.substring(containerArn.lastIndexOf('/') + 1);
            NetworkBinding containerNetwork =
                container.networkBindings().stream()
                    .filter(nb -> nb.containerPort() == containerPort)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Port not found"));

            containerDto =
                ContainerDto.builder()
                    .id(containerId)
                    .url(containerNetwork.bindIP() + ":" + containerNetwork.hostPort())
                    .status(ContainerStatus.NEW)
                    .build();
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }

        return containerDto;
    }

//    @Override
    public void update(String containerId, String gitPatch) {
        try {
            String clusterName = clusterId;
            String taskArn = taskId;

            String createPatchCommand = createPatchFileCommand(gitPatch);
            executeCommand(clusterName, taskArn, containerId, createPatchCommand);

            // Step 2: Apply the patch
            String applyCommand = "cd /app && git apply /tmp/current.patch";
            executeCommand(clusterName, taskArn, containerId, applyCommand);

            // Step 3: Clean up temporary file
            executeCommand(clusterName, taskArn, containerId, "rm /tmp/current.patch");

            System.out.println("Patch applied successfully for project: " + containerId);

        } catch (Exception e) {
            System.err.println("Failed to apply patch: " + e.getMessage());
            throw new RuntimeException("Patch application failed", e);
        }
    }

    private String createPatchFileCommand(String gitPatch) {
        String escapedPatch = gitPatch
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("$", "\\$")
            .replace("`", "\\`");

        return String.format("cat > /tmp/current.patch << 'EOF'\n%s\nEOF", escapedPatch);
    }

    private void executeCommand(String cluster, String taskArn, String container, String command) {
        ExecuteCommandRequest request = ExecuteCommandRequest.builder()
            .cluster(cluster)
            .task(taskArn)
            .container(container)
            .interactive(false)
            .command(command)
            .build();
        ecsClient.executeCommand(request);
    }
}
