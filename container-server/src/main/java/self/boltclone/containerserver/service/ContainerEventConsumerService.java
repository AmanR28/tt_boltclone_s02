package self.boltclone.containerserver.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import self.boltClone.event.container.ContainerCreateEvent;
import self.boltclone.containerserver.dto.Container;

@Service
@Slf4j
public class ContainerEventConsumerService {
    @Autowired
    ContainerService containerService;
    @Autowired
    EventProducerService eventProducerService;

    public void create(ContainerCreateEvent event) {
        Container container = containerService.create(event.projectId());
        eventProducerService.produceContainerCreateSuccessEvent(event.projectId(), container.getId());
        log.info("Event: ContainerCreateEvent Success <|> Payload ProjectId {}", event.projectId());
    }
}
