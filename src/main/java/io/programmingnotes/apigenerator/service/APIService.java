package io.programmingnotes.apigenerator.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.programmingnotes.apigenerator.data.OpenAPISummary;
import io.programmingnotes.apigenerator.data.oapi.AuthOption;
import io.programmingnotes.apigenerator.data.oapi.v3.OpenApi3;
import io.programmingnotes.apigenerator.exception.ResolutionException;
import io.programmingnotes.apigenerator.repository.OpenAPISummaryRepository;
import io.programmingnotes.apigenerator.service.oapi.OpenApi3Parser;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class APIService {
    @Autowired
    private OpenApi3Parser openAPI3Parser;
    @Autowired
    private OpenAPISummaryRepository openAPISummaryRepository;
    public Boolean uploadOapiSpec(MultipartFile multipartFile) {
        try {
            File file = File.createTempFile(multipartFile.getOriginalFilename(), ".tmp");
            URL fileURL = file.toURI().toURL();
            try {
                // Read the content of the MultipartFile as a byte array
                byte[] fileBytes = multipartFile.getBytes();

                // Convert the byte array to a JsonNode
                JsonNode jsonNode = new ObjectMapper().readTree(fileBytes);
                List<AuthOption> authOptions = new ArrayList<>();
                authOptions.add(new AuthOption(AuthOption.Type.HEADER, "api_key", "xyz", url -> url.getHost().equals("localhost")));
                OpenApi3 openAPI3 = openAPI3Parser.parse(fileURL, authOptions,jsonNode, true);
                OpenAPISummary apiSummary = new OpenAPISummary();
                apiSummary.setUserId("test");
                apiSummary.setOapi3Spec(openAPI3);
                openAPISummaryRepository.save(apiSummary);
                System.out.println(apiSummary);
            } catch (ResolutionException e) {
                throw new RuntimeException(e);
            } catch (ValidationException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
