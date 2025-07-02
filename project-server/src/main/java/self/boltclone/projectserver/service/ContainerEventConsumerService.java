package self.boltclone.projectserver.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import self.boltClone.enums.ContainerStatus;
import self.boltClone.enums.ProjectStatus;
import self.boltClone.event.container.ContainerCreateSuccessEvent;
import self.boltclone.projectserver.dto.Project;
import self.boltclone.projectserver.repository.ProjectRepository;

@Component
@Slf4j
public class ContainerEventConsumerService {
    @Autowired
    ProjectRepository projectRepository;

    public void handleContainerCreateSuccessEvent(ContainerCreateSuccessEvent event) {
        Project project = projectRepository.findById(event.projectId());
        project.setContainerId(event.containerId());
        project.setStatus(ProjectStatus.NEW.toString());
        project.setProxyUrl(event.url());
        projectRepository.save(project);
        log.info("ContainerCreateSuccessEvent Success <|> Payload ProjectId {} | ContainerId {}", event.projectId(), event.containerId());
    }

}
