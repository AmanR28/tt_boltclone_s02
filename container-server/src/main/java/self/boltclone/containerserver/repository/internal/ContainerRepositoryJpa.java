package self.boltclone.containerserver.repository.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import self.boltclone.containerserver.repository.entity.ContainerEntity;

public interface ContainerRepositoryJpa extends JpaRepository<ContainerEntity, String> {
}
