package self.boltclone.projectserver.repository.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import self.boltclone.projectserver.repository.entity.ProjectEntity;

public interface ProjectRepositoryJpa extends JpaRepository<ProjectEntity, String> {
}
