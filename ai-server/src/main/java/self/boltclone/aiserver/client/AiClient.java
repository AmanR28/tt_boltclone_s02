package self.boltclone.aiserver.client;

import self.boltclone.aiserver.dto.ResponseDto;

public interface AiClient {
    ResponseDto prompt(String prompt);
}
