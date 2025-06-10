package self.boltclone.projectserver.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Project {
    String id;
    String containerId;
    String status;
    String proxyUrl;
}
