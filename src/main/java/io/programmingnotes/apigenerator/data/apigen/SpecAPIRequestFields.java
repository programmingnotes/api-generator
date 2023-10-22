package io.programmingnotes.apigenerator.data.apigen;

import io.programmingnotes.apigenerator.service.Constants;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class SpecAPIRequestFields {
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String description;
    @Getter @Setter
    private Constants.APIMethods type;
    @Getter @Setter
    private String defaultValue;
    @Getter @Setter
    private List<SpecAPIRequestFields> childFields;
}
