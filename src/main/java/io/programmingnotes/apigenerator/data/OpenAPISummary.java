package io.programmingnotes.apigenerator.data;

import io.programmingnotes.apigenerator.data.oapi.v3.OpenApi3;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "open_api_summary")
public class OpenAPISummary {
    @Id
    @Getter @Setter
    private String id;
    @Getter @Setter
    private String userId;
    @Getter @Setter
    private OpenApi3 oapi3Spec;
}
