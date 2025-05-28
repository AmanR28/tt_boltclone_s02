package self.boltclone.projectserver.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import self.boltclone.projectserver.dto.Project;
import self.boltclone.projectserver.repository.ProjectRepository;
import self.boltclone.projectserver.repository.entity.ProjectEntity;
import self.boltclone.projectserver.repository.internal.ProjectRepositoryJpa;

import java.util.UUID;

@Repository
public class ProjectRepositoryImpl implements ProjectRepository {
    @Autowired
    ProjectRepositoryJpa repository;


    @Override
    public Project create() {
        ProjectEntity entity = new ProjectEntity();
        entity.setId(UUID.randomUUID().toString());
        return toProject(repository.save(entity));
    }

    @Override
    public String getContainerId(String projectId) {
        return repository.findById(projectId).toString();
    }

    @Override
    public void saveContainerId(String projectId, String containerId) {
        ProjectEntity entity = repository.findById(projectId).orElse(null);
        if (entity != null) {
            entity.setContainerId(containerId);
            repository.save(entity);
        }
    }

    private Project toProject(ProjectEntity entity) {
        return Project.builder().id(entity.getId()).build();
    }
}
