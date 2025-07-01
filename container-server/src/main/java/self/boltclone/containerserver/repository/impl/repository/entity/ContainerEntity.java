package self.boltclone.containerserver.repository.impl.repository.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ContainerEntity {
    @Id
    private String id;

    private String url;

    private String status;
}
