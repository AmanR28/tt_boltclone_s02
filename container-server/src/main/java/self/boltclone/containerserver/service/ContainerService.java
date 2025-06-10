package self.boltclone.containerserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import self.boltclone.containerserver.dto.Container;
import self.boltclone.containerserver.repository.ContainerRepository;

@Service
public class ContainerService {
    @Autowired
    ContainerRepository containerRepository;

    public Container create(String projectId) {
        String containerId = projectId + "-container-" + System.currentTimeMillis();
        return containerRepository.create(containerId);
    }
}
