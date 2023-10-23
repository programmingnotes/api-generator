package io.programmingnotes.apigenerator.service;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.programmingnotes.apigenerator.data.OpenAPISummary;
import io.programmingnotes.apigenerator.data.apigen.*;
import io.programmingnotes.apigenerator.data.oapi.AuthOption;
import io.programmingnotes.apigenerator.data.oapi.v3.OpenApi3;
import io.programmingnotes.apigenerator.exception.ResolutionException;
import io.programmingnotes.apigenerator.repository.OpenAPISummaryRepository;
import io.programmingnotes.apigenerator.service.oapi.OpenApi3Parser;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationException;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.swagger.v3.oas.models.OpenAPI;

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

    public Boolean createSpecFromExample(SpecByExample specExample) {
        OpenAPI openAPI = createOpenAPIHeader(specExample);
        Paths paths = new Paths();
        for(APIExample eachAPI : specExample.getSpecAPIs()) {
            Operation operation = createAPIOperation(eachAPI);
            if (eachAPI.getMethod() == Constants.APIMethods.GET){
                paths.addPathItem(eachAPI.getPath(),  new PathItem().get(operation));
            }
            if (eachAPI.getMethod() == Constants.APIMethods.POST){
                paths.addPathItem(eachAPI.getPath(),  new PathItem().post(operation));
            }
            if (eachAPI.getMethod() == Constants.APIMethods.PUT){
                paths.addPathItem(eachAPI.getPath(),  new PathItem().put(operation));
            }
            if (eachAPI.getMethod() == Constants.APIMethods.DELETE){
                paths.addPathItem(eachAPI.getPath(),  new PathItem().delete(operation));
            }
            openAPI.paths(paths);
        }
        System.out.println(openAPI);

        return false;
    }

    private Operation createAPIOperation(APIExample eachAPI) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode requestJson = null;
        Map<String, JsonNode> responseJson = new HashMap<String, JsonNode>();
        try {
            // Attempt to deserialize the JSON string into a JsonNode
            requestJson = objectMapper.readTree(eachAPI.getRequest());
            for (String key: eachAPI.getResponses().keySet()) {
                JsonNode eachResponse =  objectMapper.readTree(eachAPI.getResponses().get(key));
                responseJson.put(key, eachResponse);
            }

            // If successful, you can work with the JSON data
        } catch (JsonParseException e) {
            System.err.println("JSON parse error: " + e.getMessage());
        } catch (JsonMappingException e) {
            System.err.println("JSON mapping error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
        Operation newOpeartion = new Operation()
                .tags(List.of(eachAPI.getName()))
                .summary("Created from sample json");
        Schema requestSchema = new Schema().type("object");
        requestSchema = createSchemaFromJsonNode(requestJson, null);
        RequestBody requestBody = new RequestBody()
                .content(new Content().addMediaType("application/json", new MediaType().schema(requestSchema)));
        newOpeartion.setRequestBody(requestBody);
        ApiResponses apiResponses = new ApiResponses();
        for (String key: responseJson.keySet()) {
            Schema eachResponseSchema = new Schema().type("object");
            eachResponseSchema = createSchemaFromJsonNode(responseJson.get(key), null);
            ApiResponse eachResponse = new ApiResponse()
                    .description("API Response")
                    .content(new Content().addMediaType("application/json", new MediaType().schema(eachResponseSchema)));
            apiResponses.addApiResponse(key, eachResponse);
        }
        return newOpeartion;
    }

    private Schema createSchemaFromJsonNode(JsonNode jsonNode, String fieldName) {
        Schema schema = new Schema();

        if (jsonNode.isObject()) {
            ObjectNode objectNode = (ObjectNode) jsonNode;
            objectNode.fields().forEachRemaining(entry -> {
                String childFieldName = entry.getKey();
                JsonNode childNode = entry.getValue();
                Schema childSchema = createSchemaFromJsonNode(childNode, childFieldName);
                schema.addProperties(childFieldName, childSchema);
            });
        } else if (jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            if (arrayNode.size() > 0) {
                JsonNode firstElement = arrayNode.get(0);
                Schema itemsSchema = createSchemaFromJsonNode(firstElement, fieldName);
                schema.type("array").items(itemsSchema);
            } else {
                // Handle an empty array, you may choose to treat it as a "string" array or something else.
                schema.type("array").items(new Schema().type("string"));
            }
        } else if (jsonNode.isTextual()) {
            schema.type("string");
        } else if (jsonNode.isBoolean()) {
            schema.type("boolean");
        } else if (jsonNode.isNumber() || jsonNode.isBigDecimal() || jsonNode.isInt()) {
            schema.type("integer");
        }

        return schema;
    }

    private OpenAPI createOpenAPIHeader(SpecByExample specExample) {
        OpenAPI openAPI = new OpenAPI()
                .info(new Info()
                        .title(specExample.getName())
                        .version("1.0.0")
                        .description(specExample.getDescription())
                );
        return openAPI;
    }

    private OpenAPI createOpenAPIHeaderFromSpecSummary(SpecSummary specSummary) {
        OpenAPI openAPI = new OpenAPI()
                .info(new Info()
                        .title(specSummary.getName())
                        .version("1.0.0")
                        .description(specSummary.getDescription())
                );
        return openAPI;
    }

    public Boolean createSpec(SpecSummary specSummary) {
        OpenAPI openAPI = createOpenAPIHeaderFromSpecSummary(specSummary);
        Paths paths = new Paths();
        for (SpecAPI eachSpecAPI  : specSummary.getSpecAPIs()) {
            Operation operation = createAPIOperation(eachSpecAPI);
            if (eachSpecAPI.getType() == Constants.APIMethods.GET){
                paths.addPathItem(eachSpecAPI.getPath(),  new PathItem().get(operation));
            }
            if (eachSpecAPI.getType() == Constants.APIMethods.POST){
                paths.addPathItem(eachSpecAPI.getPath(),  new PathItem().post(operation));
            }
            if (eachSpecAPI.getType() == Constants.APIMethods.PUT){
                paths.addPathItem(eachSpecAPI.getPath(),  new PathItem().put(operation));
            }
            if (eachSpecAPI.getType() == Constants.APIMethods.DELETE){
                paths.addPathItem(eachSpecAPI.getPath(),  new PathItem().delete(operation));
            }
            openAPI.paths(paths);

        }
        return null;
    }

    private Operation createAPIOperation(SpecAPI specAPI) {

        Operation newOpeartion = new Operation()
                .tags(List.of(specAPI.getName()))
                .summary("Created from sample json");
        Schema requestSchema = new Schema().type("object");
        requestSchema = createSchemaFromRequestFields(specAPI.getRequest());
        RequestBody requestBody = new RequestBody()
                .content(new Content().addMediaType("application/json", new MediaType().schema(requestSchema)));
        newOpeartion.setRequestBody(requestBody);
        ApiResponses apiResponses = new ApiResponses();
        for (String key: specAPI.getResponses().keySet()) {
            Schema eachResponseSchema = new Schema().type("object");
            eachResponseSchema = createSchemaFromResponseFields(specAPI.getResponses().get(key));
            ApiResponse eachResponse = new ApiResponse()
                    .description("API Response")
                    .content(new Content().addMediaType("application/json", new MediaType().schema(eachResponseSchema)));
            apiResponses.addApiResponse(key, eachResponse);
        }
        return newOpeartion;
    }

    private Schema createSchemaFromRequestFields(List<SpecAPIRequestFields> requestFields) {
        Schema schema = new Schema();
        for (SpecAPIRequestFields eachReqField: requestFields) {
            if (eachReqField.getType().equals("object")) {
                Schema childSchema = createSchemaFromRequestFields(eachReqField.getChildFields());
                schema.type("object").addProperty(eachReqField.getName(), childSchema);

            } else if (eachReqField.getType().equals("array")) {
                Schema childSchema = createSchemaFromRequestFields(eachReqField.getChildFields());
                schema.type("array").addProperty(eachReqField.getName(), childSchema);

            } else if (eachReqField.getType().equals("integer")) {
                schema.type("integer");
            } else if (eachReqField.getType().equals("string")) {
                schema.type("string");
            } else if (eachReqField.getType().equals("boolean")) {
                schema.type("boolean");
            }
        }


        return schema;
    }

    private Schema createSchemaFromResponseFields(List<SpecAPIResponseFields> responseFields) {
        Schema schema = new Schema();

        for (SpecAPIResponseFields eachResField: responseFields) {
            if (eachResField.getType().equals("object")) {
                Schema childSchema = createSchemaFromResponseFields(eachResField.getChildFields());
                schema.type("object").addProperty(eachResField.getName(), childSchema);

            } else if (eachResField.getType().equals("array")) {
                Schema childSchema = createSchemaFromResponseFields(eachResField.getChildFields());
                schema.type("array").addProperty(eachResField.getName(), childSchema);

            } else if (eachResField.getType().equals("integer")) {
                schema.type("integer");
            } else if (eachResField.getType().equals("string")) {
                schema.type("string");
            } else if (eachResField.getType().equals("boolean")) {
                schema.type("boolean");
            }
        }


        return schema;
    }
}
