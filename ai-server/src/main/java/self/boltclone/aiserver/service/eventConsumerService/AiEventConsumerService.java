package self.boltclone.aiserver.service.eventConsumerService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import self.boltClone.event.ai.AiPromptEvent;
import self.boltclone.aiserver.dto.ResponseDto;
import self.boltclone.aiserver.service.AiService;
import self.boltclone.aiserver.service.EventProducerService;

@Service
@Slf4j
public class AiEventConsumerService {
    @Autowired
    AiService aiService;

    @Autowired
    EventProducerService eventProducerService;

    public void prompt(AiPromptEvent event) {

        ResponseDto response = aiService.getAiResponse(event.prompt());
        eventProducerService.produceAiPromptSuccess(event.projectId(), response.getSummary(),
            response.getCode());
        log.info("EventHandler <AiPromptEvent> <|> Payload: ProjectId {}, Summary {}, Code {}",
            event.projectId(), response.getSummary(), response.getCode());
    }

}
