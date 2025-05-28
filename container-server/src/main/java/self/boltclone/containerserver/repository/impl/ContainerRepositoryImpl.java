package self.boltclone.containerserver.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import self.boltclone.containerserver.dto.Container;
import self.boltclone.containerserver.repository.ContainerRepository;
import self.boltclone.containerserver.repository.entity.ContainerEntity;
import self.boltclone.containerserver.repository.internal.ContainerRepositoryJpa;

@Repository
public class ContainerRepositoryImpl implements ContainerRepository {
    @Autowired
    ContainerRepositoryJpa repository;

    @Override
    public Container create(String containerId) {
        ContainerEntity entity = new ContainerEntity();
        entity.setId(containerId);
        return toContainer(repository.save(entity));
    }

    private Container toContainer(ContainerEntity entity) {
        return Container.builder().id(entity.getId()).build();
    }
}
