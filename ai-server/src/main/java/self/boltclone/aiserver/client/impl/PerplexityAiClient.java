package self.boltclone.aiserver.client.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import self.boltclone.aiserver.client.AiClient;
import self.boltclone.aiserver.dto.ResponseDto;

@Slf4j
@Component
public class PerplexityAiClient implements AiClient {
    @Override
    public ResponseDto prompt(String prompt) {
        return ResponseDto.builder().response("HI").build();
    }
}
