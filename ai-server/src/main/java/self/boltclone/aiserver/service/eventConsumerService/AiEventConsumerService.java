package self.boltclone.aiserver.service.eventConsumerService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import self.boltClone.event.ai.AiPromptEvent;
import self.boltclone.aiserver.service.AiService;

@Service
@Slf4j
public class AiEventConsumerService {
    @Autowired
    AiService aiService;

    public void prompt(AiPromptEvent event) {
        String response = aiService.getAiResponse(event.prompt());
        log.info("Received AI Response for prompt {} as {}", event.prompt(), response);
    }

}
