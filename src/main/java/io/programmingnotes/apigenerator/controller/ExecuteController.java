package io.programmingnotes.apigenerator.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/execute")
public class ExecuteController {
    @GetMapping("/{username}/{spec_id}/{path:.+}")
    public String executeGet(
            @PathVariable String username,
            @PathVariable String spec_id,
            @PathVariable String path) {
        // Handle GET requests to /execute/{username}/{spec_id}/{path}
        return String.format("Executing GET for Username: %s, Spec ID: %s, Path: %s", username, spec_id, path);
    }

    @PostMapping("/{username}/{spec_id}/{path:.+}")
    public String executePost(
            @PathVariable String username,
            @PathVariable String spec_id,
            @PathVariable String path) {
        // Handle POST requests to /execute/{username}/{spec_id}/{path}
        return String.format("Executing POST for Username: %s, Spec ID: %s, Path: %s", username, spec_id, path);
    }

    @PutMapping("/{username}/{spec_id}/{path:.+}")
    public String executePut(
            @PathVariable String username,
            @PathVariable String spec_id,
            @PathVariable String path) {
        // Handle PUT requests to /execute/{username}/{spec_id}/{path}
        return String.format("Executing PUT for Username: %s, Spec ID: %s, Path: %s", username, spec_id, path);
    }
}


