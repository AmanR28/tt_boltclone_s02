package self.boltclone.aiserver.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class ResponseDto {
    String prompt;
    String summary;
    String code;
}
