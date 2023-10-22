package io.programmingnotes.apigenerator.controller;

import io.programmingnotes.apigenerator.data.apigen.SpecByExample;
import io.programmingnotes.apigenerator.data.apigen.SpecSummary;
import io.programmingnotes.apigenerator.service.APIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class APIController {
    @Autowired
    private APIService apiService;
    @PostMapping(value = "/uploadoapi", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadOpenApiSpec(@RequestParam("file") MultipartFile file) {
        // Handle the uploaded OpenAPI specification file in JSON format here.
        // You can process the file and return a response.
        Boolean result = apiService.uploadOapiSpec(file);
        return "Received and processed the OpenAPI specification file.";
    }

    @PostMapping(value = "/generatefromexample", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String generateFromExample(@RequestBody SpecByExample specExample) {
        // Handle the uploaded OpenAPI specification file in JSON format here.
        // You can process the file and return a response.
        Boolean result = apiService.createSpecFromExample(specExample);
        return "Received and processed the OpenAPI specification file.";
    }

    @PostMapping(value = "/createspec", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String generateSpec(@RequestBody SpecSummary specSummary) {
        // Handle the APISpec object and generate a JSON response.
        // You can process the API spec and return a response.
        //Boolean result = apiService.generateSpec(specSummary);
        return "Generated JSON response based on the APISpec object.";
    }}
