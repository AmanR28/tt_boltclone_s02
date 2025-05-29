package self.boltclone.projectserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import self.boltclone.projectserver.dto.request.ChatRequest;
import self.boltclone.projectserver.service.ProjectService;

@RestController
@RequestMapping("/project")
public class ApiController {
    @Autowired
    ProjectService projectService;

    @PostMapping
    public String create() {
        return projectService.create();
    }

    @PostMapping("/{project_id}/chat")
    public String chat(@PathVariable String project_id, @RequestBody ChatRequest chatRequest) {
        return projectService.chat(project_id, chatRequest.prompt());
    }
}
