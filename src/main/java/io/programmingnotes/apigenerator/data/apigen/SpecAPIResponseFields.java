package io.programmingnotes.apigenerator.data.apigen;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class SpecAPIResponseFields {
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String description;
    @Getter @Setter
    private String type;
    @Getter @Setter
    private List<SpecAPIResponseFields> childFields;

}
