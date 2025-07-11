package self.boltclone.containerserver.repository.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ContainerEntity {
    @Id
    private String projectId;

    private String containerId;

    private String url;

    private String status;

    private String name;
}
