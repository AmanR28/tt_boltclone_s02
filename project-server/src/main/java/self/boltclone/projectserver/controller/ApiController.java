package self.boltclone.projectserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import self.boltclone.projectserver.service.ProjectService;

@RestController
@RequestMapping("/project")
public class ApiController {
    @Autowired
    ProjectService projectService;

    @PostMapping
    public String createProject() {
        return projectService.create();
    }
}
