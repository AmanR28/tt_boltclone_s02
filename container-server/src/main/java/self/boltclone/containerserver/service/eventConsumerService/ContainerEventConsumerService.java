package self.boltclone.containerserver.service.eventConsumerService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import self.boltClone.event.container.ContainerCreateEvent;
import self.boltClone.event.container.ContainerUpdateEvent;
import self.boltclone.containerserver.dto.ContainerDto;
import self.boltclone.containerserver.service.ContainerService;
import self.boltclone.containerserver.service.EventProducerService;

@Service
@Slf4j
public class ContainerEventConsumerService {
    @Autowired
    ContainerService containerService;
    @Autowired
    EventProducerService eventProducerService;

    public void handleContainerCreateEvent(ContainerCreateEvent event) {

        ContainerDto containerDto = containerService.create();
        eventProducerService.produceContainerCreateSuccessEvent(event.projectId(),
            containerDto.getId(), containerDto.getUrl());
        log.info("EventHandler <ContainerCreateEvent> <|> Payload ProjectId {} | ContainerId {}", event.projectId(), containerDto.getId());
    }

    public void handleContainerUpdateEvent(ContainerUpdateEvent event) {

        Boolean success = containerService.update(event.containerId(), event.code());
        eventProducerService.produceContainerUpdateSuccessEvent(event.projectId());
        log.info("EventHandler <ContainerUpdateEvent> <|> Payload ProjectId {} | ContainerId {}", event.projectId(), event.containerId());
    }

}
