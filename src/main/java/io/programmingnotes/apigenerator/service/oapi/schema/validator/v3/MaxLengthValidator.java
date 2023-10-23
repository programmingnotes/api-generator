package io.programmingnotes.apigenerator.service.oapi.schema.validator.v3;

import com.fasterxml.jackson.databind.JsonNode;
import io.programmingnotes.apigenerator.data.oapi.v3.OAI3;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.BaseJsonValidator;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.ValidationContext;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.ValidationData;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResult;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResults;

import static io.programmingnotes.apigenerator.data.oapi.v3.OAI3SchemaKeywords.MAXLENGTH;
import static io.programmingnotes.apigenerator.service.oapi.validation.ValidationSeverity.ERROR;


/**
 * maxLength keyword validator.
 * <p/>
 * <a href="https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.2.md#schemaObject" />
 * <p/>
 * <a href="https://tools.ietf.org/html/draft-wright-json-schema-validation-00#page-7" />
 */
class MaxLengthValidator extends BaseJsonValidator<OAI3> {
  private static final ValidationResult ERR = new ValidationResult(ERROR, 1012, "Max length is '%s', found '%s'.");

  private static final ValidationResults.CrumbInfo CRUMB_INFO = new ValidationResults.CrumbInfo(MAXLENGTH, true);

  private final Integer maxLength;

  MaxLengthValidator(final ValidationContext<OAI3> context, final JsonNode schemaNode, final JsonNode schemaParentNode, final SchemaValidator parentSchema) {
    super(context, schemaNode, schemaParentNode, parentSchema);

    maxLength
      = schemaNode.isIntegralNumber()
      ? schemaNode.intValue()
      : null;
  }

  @Override
  public boolean validate(final JsonNode valueNode, final ValidationData<?> validation) {
    if (maxLength == null || !valueNode.isTextual()) {
      return false;
    }

    String value = valueNode.textValue();
    int length = value.codePointCount(0, value.length());
    if (length > maxLength) {
      validation.add(CRUMB_INFO, ERR, maxLength, length);
    }

    return false;
  }
}
