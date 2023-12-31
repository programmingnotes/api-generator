package io.programmingnotes.apigenerator.data.oapi.v3;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import static io.programmingnotes.apigenerator.data.oapi.v3.OAI3SchemaKeywords.DEFAULT;
import static io.programmingnotes.apigenerator.data.oapi.v3.OAI3SchemaKeywords.ENUM;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class ServerVariable extends AbsExtendedOpenApiSchema<ServerVariable> {
  @JsonProperty(ENUM)
  private List<String> enums;
  @JsonProperty(DEFAULT)
  private String defaultValue;
  private String description;

  // EnumValue
  public List<String> getEnums() {
    return enums;
  }

  public ServerVariable setEnums(List<String> enums) {
    this.enums = enums;
    return this;
  }

  public boolean hasEnums() {
    return enums != null;
  }

  public ServerVariable addEnum(String enumValue) {
    enums = listAdd(enums, enumValue);
    return this;
  }

  public ServerVariable insertEnum(int index, String enumValue) {
    enums = listAdd(enums, index, enumValue);
    return this;
  }

  public ServerVariable removeEnum(String enumValue) {
    listRemove(enums, enumValue);
    return this;
  }

  // Default
  public String getDefault() {
    return defaultValue;
  }

  public ServerVariable setDefault(String defaultValue) {
    this.defaultValue = defaultValue;
    return this;
  }

  // Description
  public String getDescription() {
    return description;
  }

  public ServerVariable setDescription(String description) {
    this.description = description;
    return this;
  }

  @Override
  public ServerVariable copy() {
    ServerVariable copy = new ServerVariable();

    copy.setEnums(copySimpleList(getEnums()));
    copy.setDefault(getDefault());
    copy.setDescription(getDescription());
    copy.setExtensions(copySimpleMap(getExtensions()));

    return copy;
  }
}
