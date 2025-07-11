package self.boltclone.containerserver.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import self.boltclone.containerserver.client.ContainerClient;
import self.boltclone.containerserver.dto.ContainerDto;
import self.boltclone.containerserver.repository.ContainerRepository;
import self.boltclone.containerserver.util.PatchGenerator;

import java.util.Map;

@Slf4j
@Service
public class ContainerService {
    @Autowired
    ContainerRepository containerRepository;

    @Autowired
    ContainerClient containerClient;

    public ContainerDto create(String projectId) {

        ContainerDto containerDto = containerClient.create(projectId);
        containerRepository.save(containerDto);
        return containerDto;
    }

    public void update(String projectId, String code) throws Exception {

        ContainerDto containerDto = containerRepository.findByProjectId(projectId);
        PatchGenerator patchGenerator = new PatchGenerator();
        Map<String, String> codeMap = patchGenerator.parseCode(code);
        String gitPatch = patchGenerator.generateGitPatch(codeMap);

        System.out.println("Generated Git Patch:\n" + gitPatch);
        containerClient.update(containerDto.getProjectId(), containerDto.getName(), gitPatch);

        log.info("Updating container with ID: {}, code: {}", projectId, code);
    }

}
