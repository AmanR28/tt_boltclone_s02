package self.boltclone.containerserver.client;

import self.boltclone.containerserver.dto.ContainerDto;

public interface ContainerClient {
    ContainerDto create(String projectId);

    void update(String projectId, String containerName, String gitPatch);

}
