package self.boltclone.containerserver.dto;

import lombok.Builder;
import lombok.Data;
import self.boltClone.enums.ContainerStatus;

@Data
@Builder
public class ContainerDto {
    private String id;
    private String url;
    private ContainerStatus status;
}
