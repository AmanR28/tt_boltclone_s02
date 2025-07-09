package self.boltclone.containerserver.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public ContainerDto create() {

        ContainerDto containerDto = containerClient.create();
        containerRepository.save(containerDto);
        return containerDto;
    }

    public void update(String containerId, String code) throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        PatchGenerator patchGenerator = new PatchGenerator();
        Map<String, String> codeMap = patchGenerator.parseCode(code);
        String gitPatch = patchGenerator.generateGitPatch(codeMap);

        System.out.println("Generated Git Patch:\n" + gitPatch);

        log.info("Updating container with ID: {}, code: {}", containerId, code);
    }

}
