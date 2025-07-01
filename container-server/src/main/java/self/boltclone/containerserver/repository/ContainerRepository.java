package self.boltclone.containerserver.repository;

import self.boltclone.containerserver.dto.ContainerDto;

public interface ContainerRepository {
    void save(ContainerDto containerDto);
}
