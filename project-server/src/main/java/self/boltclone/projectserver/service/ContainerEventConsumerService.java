package self.boltclone.projectserver.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import self.boltClone.event.container.ContainerCreateSuccessEvent;
import self.boltclone.projectserver.repository.ProjectRepository;

@Component
@Slf4j
public class ContainerEventConsumerService {
    @Autowired
    ProjectRepository projectRepository;

    public void handleContainerCreateSuccessEvent(ContainerCreateSuccessEvent event) {
        projectRepository.saveContainerId(event.projectId(), event.containerId());
        log.info("Event: ContainerCreateSuccessEvent Success <|> Payload ProjectId {} | ContainerId {}", event.projectId(), event.containerId());
    }

}
