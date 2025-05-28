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
    private String id;
}
