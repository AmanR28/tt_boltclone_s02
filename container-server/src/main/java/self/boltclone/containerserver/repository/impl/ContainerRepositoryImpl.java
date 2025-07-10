package self.boltclone.containerserver.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import self.boltclone.containerserver.dto.ContainerDto;
import self.boltclone.containerserver.repository.ContainerRepository;
import self.boltclone.containerserver.repository.entity.ContainerEntity;
import self.boltclone.containerserver.repository.internal.ContainerRepositoryJpa;

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
        entity.setName(containerDto.getName());
        repository.save(entity);
    }

    @Override
    public ContainerDto findById(String id) {
        ContainerEntity entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Container not found"));
        return toContainer(entity);
    }

    private ContainerDto toContainer(ContainerEntity entity) {

        return ContainerDto.builder().id(entity.getId()).name(entity.getName()).build();
    }

}
