package io.programmingnotes.apigenerator.data.apigen;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class SpecByExample {
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String description;
    @Getter @Setter
    private List<APIExample> specAPIs;
}

