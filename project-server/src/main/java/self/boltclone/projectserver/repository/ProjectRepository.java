package self.boltclone.projectserver.repository;


import org.springframework.stereotype.Repository;
import self.boltclone.projectserver.dto.Project;

@Repository
public interface ProjectRepository {
    Project create();

    Project findById(String projectId);

    void save(Project project);
}
