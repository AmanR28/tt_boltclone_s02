package self.boltclone.aiserver.client.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import self.boltclone.aiserver.client.AiClient;

@Slf4j
@Component
public class PerplexityAiClient implements AiClient {
    @Override
    public String prompt(String prompt) {
        return "Perplexity AI response for prompt: " + prompt;
    }
}
