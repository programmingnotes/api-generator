package io.programmingnotes.apigenerator.data.apigen;

import io.programmingnotes.apigenerator.service.Constants;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

public class SpecAPI {
    @Getter @Setter
    private String path;
    @Getter @Setter
    private Constants.APIMethods type;
    @Getter @Setter
    private List<SpecAPIRequestFields> request;
    @Getter @Setter
    private Map<String, List<SpecAPIResponseFields>> responses;
    @Getter @Setter
    private String validationExpression;
}
