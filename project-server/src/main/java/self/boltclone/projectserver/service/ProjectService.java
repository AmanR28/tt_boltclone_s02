package self.boltclone.projectserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public void chat(String prompt, String projectId) {
    }
}
