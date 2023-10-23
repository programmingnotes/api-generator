package io.programmingnotes.apigenerator.service.oapi.schema.validator.v3;

import com.fasterxml.jackson.databind.JsonNode;
import io.programmingnotes.apigenerator.data.oapi.v3.OAI3;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.BaseJsonValidator;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.ValidationContext;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.ValidationData;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResult;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResults;

import static io.programmingnotes.apigenerator.data.oapi.v3.OAI3SchemaKeywords.*;
import static io.programmingnotes.apigenerator.service.oapi.validation.ValidationSeverity.ERROR;


/**
 * type keyword validator.
 * <p/>
 * <a href="https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.2.md#schemaObject" />
 * <p/>
 * <a href="https://tools.ietf.org/html/draft-wright-json-schema-validation-00#page-11" />
 */
class TypeValidator extends BaseJsonValidator<OAI3> {
  private static final ValidationResult ERR = new ValidationResult(ERROR, 1027, "Type expected '%s', found '%s'.");

  private static final ValidationResults.CrumbInfo CRUMB_INFO = new ValidationResults.CrumbInfo(TYPE, true);

  // non-OAS type
  private static final String TYPE_NULL = "null";

  private final String type;

  TypeValidator(final ValidationContext<OAI3> context, final JsonNode schemaNode, final JsonNode schemaParentNode, final SchemaValidator parentSchema) {
    super(context, schemaNode, schemaParentNode, parentSchema);

    type = schemaNode.textValue();
  }

  @Override
  public boolean validate(final JsonNode valueNode, final ValidationData<?> validation) {
    String valueType = getTypeFromValue(valueNode);
    if (!valueType.equals(type)) {
      if (TYPE_NUMBER.equals(type) && TYPE_INTEGER.equals(valueType)) {
        // number includes integer
        // https://tools.ietf.org/html/draft-wright-json-schema-validation-00#page-11
        return false;
      }

      if (!TYPE_NULL.equals(valueType)) {
        validation.add(CRUMB_INFO, ERR, type, valueType);
      }
    }

    return false;
  }

  private String getTypeFromValue(JsonNode valueNode) {
    if (valueNode.isContainerNode()) {
      return valueNode.isObject() ? TYPE_OBJECT : TYPE_ARRAY;
    }

    if (valueNode.isTextual())
      return TYPE_STRING;
    else if (valueNode.isIntegralNumber())
      return TYPE_INTEGER;
    else if (valueNode.isNumber())
      return TYPE_NUMBER;
    else if (valueNode.isBoolean())
      return TYPE_BOOLEAN;
    else
      return TYPE_NULL;
  }
}
