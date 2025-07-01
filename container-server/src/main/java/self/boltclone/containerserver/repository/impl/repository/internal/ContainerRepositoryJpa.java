package self.boltclone.containerserver.repository.impl.repository.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import self.boltclone.containerserver.repository.impl.repository.entity.ContainerEntity;

public interface ContainerRepositoryJpa extends JpaRepository<ContainerEntity, String> {
}
