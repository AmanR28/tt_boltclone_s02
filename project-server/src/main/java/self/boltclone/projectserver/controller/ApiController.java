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

    @GetMapping("/{project_id}")
    public String status(@PathVariable String project_id) {
        return projectService.status(project_id);
    }

    @GetMapping("/{project_id}/preview")
    public String preview(@PathVariable String project_id) {
        return projectService.preview(project_id);
    }

    @PostMapping("/{project_id}/prompt")
    public String prompt(@PathVariable String project_id, @RequestBody ChatRequest chatRequest) {
        return "";
    }

    @GetMapping("/{project_id}/response")
    public String response(@PathVariable String project_id) {
        return "";
    }
}
