package self.boltclone.containerserver.repository.impl.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import self.boltclone.containerserver.repository.impl.dto.ContainerDto;
import self.boltclone.containerserver.repository.impl.repository.ContainerRepository;
import self.boltclone.containerserver.repository.impl.repository.entity.ContainerEntity;
import self.boltclone.containerserver.repository.impl.repository.internal.ContainerRepositoryJpa;

@Repository
public class ContainerRepositoryImpl implements ContainerRepository {
    @Autowired
    ContainerRepositoryJpa repository;

    @Override
    public void save(ContainerDto containerDto) {
        ContainerEntity entity = new ContainerEntity();
        entity.setId(containerDto.getId());
        entity.setUrl(containerDto.getUrl());
        entity.setStatus(containerDto.getStatus().toString());
        repository.save(entity);
    }

    private ContainerDto toContainer(ContainerEntity entity) {

        return ContainerDto.builder().id(entity.getId()).build();
    }

}
