package self.boltclone.containerserver.service;

import org.springframework.stereotype.Service;
import self.boltClone.event.container.ContainerCreateEvent;

@Service
public class ContainerService {
    public void create(ContainerCreateEvent event) {
        System.out.println("Creating container for project ID: " + event.projectId());
    }
}
