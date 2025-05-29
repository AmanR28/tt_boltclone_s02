package self.boltclone.projectserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import self.boltclone.projectserver.dto.request.ChatRequest;
import self.boltclone.projectserver.service.ProjectService;
import self.boltclone.projectserver.service.SocketService;

@RestController
@RequestMapping("/project")
public class ApiController {
    @Autowired
    ProjectService projectService;
    @Autowired
    SocketService socketService;

    @PostMapping
    public String create() {
        return projectService.create();
    }

    @GetMapping("/{project_id}")
    public String getProject(@PathVariable String project_id) {
        Integer status = projectService.status(project_id);
        if (status == 404) {
            return "Project not found";
        } else if (status == 403) {
            return "Project is not ready";
        } else if (status == 200) {
            socketService.sendMessageToProject(project_id, "CONTAINER_READY");
            return "Project is ready";
        }
        return null;
    }


    @PostMapping("/{project_id}/chat")
    public String chat(@PathVariable String project_id, @RequestBody ChatRequest chatRequest) {
        return projectService.chat(project_id, chatRequest.prompt());
    }
}
