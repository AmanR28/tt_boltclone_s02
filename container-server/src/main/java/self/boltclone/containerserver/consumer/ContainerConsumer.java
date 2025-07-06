package self.boltclone.containerserver.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import self.boltClone.contant.KafkaConstant;
import self.boltClone.event.container.ContainerCreateEvent;
import self.boltClone.event.container.ContainerUpdateEvent;
import self.boltclone.containerserver.service.eventConsumerService.ContainerEventConsumerService;

@Slf4j
@Component
public class ContainerConsumer {
    @Autowired
    ContainerEventConsumerService containerEventConsumerService;

    @KafkaListener(
        topics = KafkaConstant.TOPIC_CONTAINER,
        groupId = KafkaConstant.GROUP_CONTAINER,
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(ConsumerRecord<String, Object> record) {

        Object payload = record.value();

        try {
            if (payload.getClass().equals(ContainerCreateEvent.class))
                containerEventConsumerService.handleContainerCreateEvent(
                    (ContainerCreateEvent) payload);
            else
                if (payload.getClass().equals(ContainerUpdateEvent.class))
                    containerEventConsumerService.handleContainerUpdateEvent(
                        (ContainerUpdateEvent) payload);
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
            throw new RuntimeException("Error processing message", e);
        }
    }

}
