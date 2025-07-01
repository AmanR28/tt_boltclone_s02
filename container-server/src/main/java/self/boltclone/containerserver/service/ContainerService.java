package self.boltclone.containerserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import self.boltclone.containerserver.client.ContainerClient;
import self.boltclone.containerserver.dto.ContainerDto;
import self.boltclone.containerserver.repository.ContainerRepository;

@Service
public class ContainerService {
    @Autowired
    ContainerRepository containerRepository;

    @Autowired
    ContainerClient containerClient;

    public ContainerDto create() {
        ContainerDto containerDto = containerClient.create();
        containerRepository.save(containerDto);
        return containerDto;
    }

}
