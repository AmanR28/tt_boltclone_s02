package self.boltclone.projectserver.service.eventConsumerService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import self.boltClone.enums.ProjectStatus;
import self.boltClone.event.ai.AiPromptSuccessEvent;
import self.boltclone.projectserver.dto.Project;
import self.boltclone.projectserver.repository.ProjectRepository;
import self.boltclone.projectserver.service.EventProducerService;

@Slf4j
@Component
public class AiEventConsumerService {
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    EventProducerService eventProducerService;

    public void handleAiPromptSuccessEvent(AiPromptSuccessEvent event) {

        Project project = projectRepository.findById(event.projectId());
        project.setStatus(ProjectStatus.PROCESS_CODE.toString());
        projectRepository.save(project);
        eventProducerService.sendContainerUpdateEvent(event.projectId(), project.getContainerId(),
            event.code());
        log.info(
            "EventHandler <AiPromptSuccessEvent> <|> Payload ProjectId {} | Summary {} | Code {}",
            event.projectId(), event.summary(), event.code());
    }

}
