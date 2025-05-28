package self.boltclone.containerserver.repository;

import self.boltclone.containerserver.dto.Container;

public interface ContainerRepository {
    Container create(String containerId);
}
