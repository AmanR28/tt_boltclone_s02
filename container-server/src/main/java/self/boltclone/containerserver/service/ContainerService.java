package self.boltclone.containerserver.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import self.boltclone.containerserver.client.ContainerClient;
import self.boltclone.containerserver.dto.ContainerDto;
import self.boltclone.containerserver.repository.ContainerRepository;

@Slf4j
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

    public Boolean update(String containerId, String code) {

        log.info("Updating container with ID: {}, code: {}", containerId, code);
        return true;
    }

}
