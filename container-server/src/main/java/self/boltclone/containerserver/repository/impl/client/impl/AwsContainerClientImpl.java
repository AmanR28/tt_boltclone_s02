package self.boltclone.containerserver.repository.impl.client.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import self.boltClone.enums.ContainerStatus;
import self.boltclone.containerserver.repository.impl.client.ContainerClient;
import self.boltclone.containerserver.repository.impl.dto.ContainerDto;
import software.amazon.awssdk.services.ecs.EcsClient;
import software.amazon.awssdk.services.ecs.model.*;

@Slf4j
@Component
public class AwsContainerClientImpl implements ContainerClient {
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

        try {
            RunTaskResponse response =
                ecsClient.runTask(
                    RunTaskRequest.builder()
                        .cluster(clusterId)
                        .launchType(LaunchType.FARGATE)
                        .taskDefinition(taskId)
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

}
