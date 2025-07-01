package self.boltclone.containerserver.service.eventConsumerService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import self.boltClone.event.container.ContainerCreateEvent;
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

    public void create(ContainerCreateEvent event) {
        ContainerDto containerDto = containerService.create();
        eventProducerService.produceContainerCreateSuccessEvent(event.projectId(),
            containerDto.getId(), containerDto.getUrl());
    }

}
