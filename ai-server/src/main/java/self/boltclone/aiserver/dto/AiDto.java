package self.boltclone.aiserver.dto;

import lombok.Builder;
import lombok.Data;
import self.boltClone.enums.ContainerStatus;

@Data
@Builder
public class AiDto {
    private String id;
    private String url;
    private ContainerStatus status;
}
