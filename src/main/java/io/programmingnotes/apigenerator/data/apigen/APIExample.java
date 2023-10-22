package io.programmingnotes.apigenerator.data.apigen;

import io.programmingnotes.apigenerator.service.Constants;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

public class APIExample {
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String description;
    @Getter @Setter
    private String path;
    @Getter @Setter
    private Constants.APIMethods method;
    @Getter @Setter
    private String request;
    @Getter @Setter
    private Map<String, String> responses;
    @Getter @Setter
    private String validationExpression;
}
