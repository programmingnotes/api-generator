package io.programmingnotes.apigenerator.controller;

import io.programmingnotes.apigenerator.service.APIService;
import io.programmingnotes.apigenerator.service.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/execute")
public class ExecuteController {
    @Autowired
    private APIService apiService;
    @GetMapping("/{username}/{spec_id}/{path:.+}")
    public String executeGet(
            @PathVariable String username,
            @PathVariable String specId,
            @PathVariable String path) {
        // Handle GET requests to /execute/{username}/{spec_id}/{path}
        apiService.executeService(Constants.APIMethods.GET, username, specId, path, null);

        return String.format("Executing GET for Username: %s, Spec ID: %s, Path: %s", username, specId, path);
    }

    @PostMapping("/{username}/{spec_id}/{path:.+}")
    public String executePost(
            @PathVariable String username,
            @PathVariable String specId,
            @PathVariable String path,
            @RequestBody String requestBody) {
        // Handle POST requests to /execute/{username}/{spec_id}/{path}
        apiService.executeService(Constants.APIMethods.POST, username, specId, path, requestBody);
        return String.format("Executing POST for Username: %s, Spec ID: %s, Path: %s", username, specId, path);
    }

    @PutMapping("/{username}/{spec_id}/{path:.+}")
    public String executePut(
            @PathVariable String username,
            @PathVariable String specId,
            @PathVariable String path,
            @RequestBody String requestBody) {
        // Handle PUT requests to /execute/{username}/{spec_id}/{path}
        apiService.executeService(Constants.APIMethods.POST, username, specId, path, requestBody);
        return String.format("Executing PUT for Username: %s, Spec ID: %s, Path: %s", username, specId, path);
    }
}


