package self.boltclone.aiserver.client.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import self.boltclone.aiserver.client.AiClient;

@Slf4j
@Component
@Primary
public class MockAiClient implements AiClient {

    @Override
    public String prompt(String prompt) {
        return "Mock response for prompt: " + prompt;
    }

}
