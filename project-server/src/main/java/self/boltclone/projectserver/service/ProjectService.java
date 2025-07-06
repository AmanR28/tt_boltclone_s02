package self.boltclone.projectserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import self.boltClone.enums.ProjectStatus;
import self.boltclone.projectserver.dto.Project;
import self.boltclone.projectserver.repository.ProjectRepository;

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

    public String status(String projectId) {

        return projectRepository.findById(projectId).getStatus();
    }

    public String preview(String projectId) {

        return projectRepository.findById(projectId).getProxyUrl();
    }

    public void prompt(String projectId, String prompt) {

        Project project = projectRepository.findById(projectId);
        if (!project.getStatus().equals(ProjectStatus.ACTIVE.toString()))
            throw new RuntimeException("Project is already active, cannot process AI prompt.");
        project.setStatus(ProjectStatus.PROCESS_AI.toString());
        projectRepository.save(project);
        eventProducerService.sendAiPromptEvent(projectId, null, prompt);
    }

}
