package self.boltclone.projectserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import self.boltclone.projectserver.dto.Project;
import self.boltclone.projectserver.repository.ProjectRepository;

import java.util.UUID;

@Service
public class ProjectService {
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    EventProducerService eventProducerService;

    public String create() {
        Project project = projectRepository.create();
        eventProducerService.sendContainerCreateEvent(project.getId());
        return project.getId();
    }

    public String chat(String projectId, String prompt) {
        String chatId = UUID.randomUUID().toString();
        String containerId = projectRepository.getContainerId(projectId);
        eventProducerService.sendAiPromptEvent(containerId, chatId, prompt);
        return chatId;
    }
}
