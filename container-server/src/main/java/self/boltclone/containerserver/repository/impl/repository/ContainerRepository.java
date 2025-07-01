package self.boltclone.containerserver.repository.impl.repository;

import self.boltclone.containerserver.repository.impl.dto.ContainerDto;

public interface ContainerRepository {
    void save(ContainerDto containerDto);
}
