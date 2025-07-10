package self.boltclone.containerserver.client;

import self.boltclone.containerserver.dto.ContainerDto;

public interface ContainerClient {
    ContainerDto create();

    void update(String containerId, String gitPatch);

}
