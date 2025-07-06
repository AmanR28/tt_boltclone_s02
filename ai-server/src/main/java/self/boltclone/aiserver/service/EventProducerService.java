package self.boltclone.aiserver.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import self.boltClone.contant.KafkaConstant;
import self.boltClone.enums.EventConstant;
import self.boltClone.event.ai.AiPromptSuccessEvent;

@Service
@Slf4j
public class EventProducerService {
    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    public void produceAiPromptSuccess(String projectId, String summary, String code) {

        AiPromptSuccessEvent event =
            new AiPromptSuccessEvent(projectId, summary, code);

        Message<AiPromptSuccessEvent> message = MessageBuilder
            .withPayload(event)
            .setHeader(KafkaHeaders.TOPIC, KafkaConstant.TOPIC_AI)
            .setHeader(KafkaConstant.HEADER, EventConstant.AI_PROMPT_SUCCESS)
            .build();

        kafkaTemplate.send(message);
        log.info(
            "Event Produced <AiPromptSuccessEvent> <|> Payload: ProjectId {}, Summary {}, Code {}",
            projectId, summary, code);
    }

}
