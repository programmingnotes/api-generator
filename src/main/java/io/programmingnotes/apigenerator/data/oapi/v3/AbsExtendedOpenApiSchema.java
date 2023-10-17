package io.programmingnotes.apigenerator.data.oapi.v3;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import io.programmingnotes.apigenerator.data.oapi.AbsOpenApiSchema;
import io.programmingnotes.apigenerator.data.oapi.OpenApiSchema;


import java.util.HashMap;
import java.util.Map;

public abstract class AbsExtendedOpenApiSchema<M extends OpenApiSchema<M>> extends AbsOpenApiSchema<M> {
  private Map<String, Object> extensions;

  // Extensions
  @JsonAnyGetter
  public Map<String, Object> getExtensions() {
    return extensions;
  }

  public void setExtensions(Map<String, Object> extensions) {
    this.extensions = extensions;
  }

  @JsonAnySetter
  public void setExtension(String name, Object value) {
    if (extensions == null) {
      extensions = new HashMap<>();
    }
    extensions.put(name, value);
  }
}
