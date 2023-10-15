package io.programmingnotes.apigenerator.controller;

import io.programmingnotes.apigenerator.data.apigen.SpecSummary;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class APIController {
    @PostMapping(value = "/uploadoapi", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadOpenApiSpec(@RequestParam("file") MultipartFile file) {
        // Handle the uploaded OpenAPI specification file in JSON format here.
        // You can process the file and return a response.
        return "Received and processed the OpenAPI specification file.";
    }

    @PostMapping(value = "/generate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String generateApiSpec(@RequestBody SpecSummary specSummary) {
        // Handle the APISpec object and generate a JSON response.
        // You can process the API spec and return a response.
        return "Generated JSON response based on the APISpec object.";
    }}
