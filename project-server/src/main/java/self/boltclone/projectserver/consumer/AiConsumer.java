package self.boltclone.projectserver.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import self.boltClone.contant.KafkaConstant;
import self.boltClone.event.ai.AiPromptSuccessEvent;
import self.boltclone.projectserver.service.eventConsumerService.AiEventConsumerService;

@Slf4j
@Component
public class AiConsumer {
    @Autowired
    AiEventConsumerService aiEventConsumerService;

    @KafkaListener(
        topics = KafkaConstant.TOPIC_AI,
        groupId = KafkaConstant.GROUP_PROJECT,
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(ConsumerRecord<String, Object> record) {

        Object payload = record.value();

        try {
            if (payload.getClass().equals(AiPromptSuccessEvent.class))
                aiEventConsumerService.handleAiPromptSuccessEvent((AiPromptSuccessEvent) payload);
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
            throw new RuntimeException("Error processing message", e);
        }
    }

}
