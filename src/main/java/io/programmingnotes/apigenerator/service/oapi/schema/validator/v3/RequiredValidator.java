package io.programmingnotes.apigenerator.service.oapi.schema.validator.v3;

import com.fasterxml.jackson.databind.JsonNode;
import io.programmingnotes.apigenerator.data.oapi.v3.OAI3;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.BaseJsonValidator;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.ValidationContext;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.ValidationData;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResult;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResults;


import java.util.ArrayList;
import java.util.List;

import static io.programmingnotes.apigenerator.data.oapi.v3.OAI3SchemaKeywords.REQUIRED;
import static io.programmingnotes.apigenerator.service.oapi.validation.ValidationSeverity.ERROR;


/**
 * required keyword validator.
 * <p/>
 * <a href="https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.2.md#schemaObject" />
 * <p/>
 * <a href="https://tools.ietf.org/html/draft-wright-json-schema-validation-00#page-9" />
 */
class RequiredValidator extends BaseJsonValidator<OAI3> {
  private static final ValidationResult ERR = new ValidationResult(ERROR, 1026, "Field '%s' is required.");

  private static final ValidationResults.CrumbInfo CRUMB_INFO = new ValidationResults.CrumbInfo(REQUIRED, true);

  private final List<String> fieldNames;

  RequiredValidator(final ValidationContext<OAI3> context, final JsonNode schemaNode, final JsonNode schemaParentNode, final SchemaValidator parentSchema) {
    super(context, schemaNode, schemaParentNode, parentSchema);

    if (schemaNode.isArray()) {
      fieldNames = new ArrayList<>();

      for (JsonNode fieldName : schemaNode) {
        fieldNames.add(fieldName.asText());
      }
    } else {
      fieldNames = null;
    }
  }

  @Override
  public boolean validate(final JsonNode valueNode, final ValidationData<?> validation) {
    if (fieldNames == null) return false;

    for (String fieldName : fieldNames) {
      if (null == valueNode.get(fieldName)) {
        validation.add(CRUMB_INFO, ERR, fieldName);
      }
    }

    return false;
  }
}
