package self.boltclone.projectserver.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import self.boltClone.enums.EventConstant;
import self.boltClone.contant.KafkaConstant;
import self.boltClone.event.ai.AiPromptEvent;
import self.boltClone.event.container.ContainerCreateEvent;
import self.boltClone.event.container.ContainerUpdateEvent;

@Service
@Slf4j
public class EventProducerService {
    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    public void sendContainerCreateEvent(String projectId) {
        ContainerCreateEvent event = new ContainerCreateEvent(projectId);

        Message<ContainerCreateEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, KafkaConstant.TOPIC_CONTAINER)
                .setHeader(KafkaConstant.HEADER, EventConstant.CONTAINER_CREATE)
                .build();

        kafkaTemplate.send(message);
        log.info("Event Produced <ContainerCreateEvent> <|> Payload: ProjectId {}", projectId);
    }

    public void sendContainerUpdateEvent(String projectId, String containerId, String code) {
        ContainerUpdateEvent event = new ContainerUpdateEvent(projectId, containerId, code);

        Message<ContainerUpdateEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, KafkaConstant.TOPIC_CONTAINER)
                .setHeader(KafkaConstant.HEADER, EventConstant.CONTAINER_UPDATE)
                .build();

        kafkaTemplate.send(message);
        log.info("Event Produced <ContainerUpdateEvent> <|> Payload: ProjectId {}, ContainerId {}", projectId, containerId);
    }

    public void sendAiPromptEvent(String projectId, String chatId, String prompt) {
        AiPromptEvent event = new AiPromptEvent(projectId, chatId, prompt);

        Message<AiPromptEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, KafkaConstant.TOPIC_AI)
                .setHeader(KafkaConstant.HEADER, EventConstant.AI_PROMPT)
                .build();

        kafkaTemplate.send(message);
        log.info("Event Produced <AiPromptEvent> <|> Payload: ProjectId {}, ChatId {}, Prompt {}", projectId, chatId, prompt);
    }
}
