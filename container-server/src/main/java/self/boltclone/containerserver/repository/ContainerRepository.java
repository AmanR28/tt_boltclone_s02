package self.boltclone.containerserver.repository;

import self.boltclone.containerserver.dto.ContainerDto;

public interface ContainerRepository {
    ContainerDto findByProjectId(String projectId);
    void save(ContainerDto containerDto);
}
