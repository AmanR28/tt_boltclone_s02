package self.boltclone.projectserver.service.eventConsumerService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import self.boltClone.enums.ProjectStatus;
import self.boltClone.event.container.ContainerCreateSuccessEvent;
import self.boltClone.event.container.ContainerUpdateSuccessEvent;
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
        project.setStatus(ProjectStatus.ACTIVE.toString());
        project.setProxyUrl(event.url());
        projectRepository.save(project);
        log.info(
            "EventHandler <ContainerCreateSuccessEvent> <|> Payload ProjectId {} | ContainerId {} | URL {}",
            event.projectId(), event.containerId(), event.url());
    }

    public void handleContainerUpdateSuccessEvent(ContainerUpdateSuccessEvent event) {

        Project project = projectRepository.findById(event.projectId());
        project.setStatus(ProjectStatus.ACTIVE.toString());
        projectRepository.save(project);
        log.info(
            "EventHandler <ContainerUpdateSuccessEvent> <|> Payload ProjectId {} ",
            event.projectId());
    }

}
