package self.boltclone.projectserver.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import self.boltClone.contant.KafkaConstant;
import self.boltClone.event.container.ContainerCreateSuccessEvent;
import self.boltClone.event.container.ContainerUpdateSuccessEvent;
import self.boltclone.projectserver.service.eventConsumerService.ContainerEventConsumerService;

@Slf4j
@Component
public class ContainerConsumer {
    @Autowired
    ContainerEventConsumerService containerEventConsumerService;

    @KafkaListener(
            topics = KafkaConstant.TOPIC_CONTAINER,
            groupId = KafkaConstant.GROUP_PROJECT,
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(ConsumerRecord<String, Object> record) {
        Object payload = record.value();

        try {
            if (payload.getClass().equals(ContainerCreateSuccessEvent.class))
                containerEventConsumerService.handleContainerCreateSuccessEvent((ContainerCreateSuccessEvent) payload);
            if (payload.getClass().equals(ContainerUpdateSuccessEvent.class))
                containerEventConsumerService.handleContainerUpdateSuccessEvent((ContainerUpdateSuccessEvent) payload);
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
            throw new RuntimeException("Error processing message", e);
        }
    }

}
