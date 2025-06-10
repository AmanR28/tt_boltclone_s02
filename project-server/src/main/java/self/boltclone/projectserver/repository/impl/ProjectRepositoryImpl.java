package self.boltclone.projectserver.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import self.boltClone.enums.ContainerStatus;
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
        entity.setStatus(ContainerStatus.NEW.toString());
        return toProject(repository.save(entity));
    }

    @Override
    public Project findById(String projectId) {
        return toProject(repository.findById(projectId).orElse(null));
    }

    @Override
    public void save(Project project) {
        repository.save(toEntity(project));
    }

    private ProjectEntity toEntity(Project project) {
        if (project == null) return null;
        ProjectEntity entity = new ProjectEntity();
        entity.setId(project.getId());
        entity.setContainerId(project.getContainerId());
        entity.setStatus(project.getStatus());
        entity.setProxyUrl(project.getProxyUrl());
        return entity;
    }

    private Project toProject(ProjectEntity entity) {
        if (entity == null) return null;
        return Project.builder().id(entity.getId()).containerId(entity.getContainerId()).status(entity.getStatus()).proxyUrl(entity.getStatus()).build();
    }
}
