package io.programmingnotes.apigenerator.service.oapi.schema.validator.v3;

import com.fasterxml.jackson.databind.JsonNode;
import io.programmingnotes.apigenerator.data.oapi.v3.OAI3;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.BaseJsonValidator;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.ValidationContext;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.ValidationData;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResult;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResults;

import static io.programmingnotes.apigenerator.data.oapi.v3.OAI3SchemaKeywords.MINITEMS;
import static io.programmingnotes.apigenerator.service.oapi.validation.ValidationSeverity.ERROR;


/**
 * minItems keyword validator.
 * <p/>
 * <a href="https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.2.md#schemaObject" />
 * <p/>
 * <a href="https://tools.ietf.org/html/draft-wright-json-schema-validation-00#page-8" />
 */
class MinItemsValidator extends BaseJsonValidator<OAI3> {
  private static final ValidationResult ERR = new ValidationResult(ERROR, 1016, "Min items is '%s', found '%s'.");

  private static final ValidationResults.CrumbInfo CRUMB_INFO = new ValidationResults.CrumbInfo(MINITEMS, true);

  private final Integer min;

  MinItemsValidator(final ValidationContext<OAI3> context, final JsonNode schemaNode, final JsonNode schemaParentNode, final SchemaValidator parentSchema) {
    super(context, schemaNode, schemaParentNode, parentSchema);

    min
      = schemaNode.isIntegralNumber()
      ? schemaNode.intValue()
      : null;
  }

  @Override
  public boolean validate(final JsonNode valueNode, final ValidationData<?> validation) {
    if (min == null || !valueNode.isArray()) {
      return false;
    }

    if (valueNode.size() < min) {
      validation.add(CRUMB_INFO, ERR, min, valueNode.size());
    }

    return false;
  }
}
