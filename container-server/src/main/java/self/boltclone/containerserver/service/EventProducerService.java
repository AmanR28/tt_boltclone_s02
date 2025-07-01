package self.boltclone.containerserver.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import self.boltClone.contant.KafkaConstant;
import self.boltClone.enums.EventConstant;
import self.boltClone.event.container.ContainerCreateSuccessEvent;

@Service
@Slf4j
public class EventProducerService {
    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    public void produceContainerCreateSuccessEvent(String projectId, String containerId,
        String url) {

        ContainerCreateSuccessEvent event =
            new ContainerCreateSuccessEvent(projectId, containerId, url);

        Message<ContainerCreateSuccessEvent> message = MessageBuilder
            .withPayload(event)
            .setHeader(KafkaHeaders.TOPIC, KafkaConstant.TOPIC_CONTAINER)
            .setHeader(KafkaConstant.HEADER, EventConstant.CONTAINER_CREATE)
            .build();

        kafkaTemplate.send(message);
    }

}
