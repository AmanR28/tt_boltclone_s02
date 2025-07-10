package self.boltclone.containerserver.repository;

import self.boltclone.containerserver.dto.ContainerDto;

public interface ContainerRepository {
    ContainerDto findById(String id);
    void save(ContainerDto containerDto);
}
