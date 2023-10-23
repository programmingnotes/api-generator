package io.programmingnotes.apigenerator.service.oapi.schema.validator.v3;

import com.fasterxml.jackson.databind.JsonNode;
import io.programmingnotes.apigenerator.data.oapi.v3.OAI3;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.BaseJsonValidator;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.ValidationContext;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.ValidationData;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResult;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResults;


import java.math.BigDecimal;

import static io.programmingnotes.apigenerator.data.oapi.v3.OAI3SchemaKeywords.EXCLUSIVEMINIMUM;
import static io.programmingnotes.apigenerator.data.oapi.v3.OAI3SchemaKeywords.MINIMUM;
import static io.programmingnotes.apigenerator.service.oapi.validation.ValidationSeverity.ERROR;


/**
 * minimum keyword validator.
 * <p/>
 * <a href="https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.2.md#schemaObject" />
 * <p/>
 * <a href="https://tools.ietf.org/html/draft-wright-json-schema-validation-00#page-6" />
 */
class MinimumValidator extends BaseJsonValidator<OAI3> {
  private static final ValidationResult EXCLUSIVE_ERR = new ValidationResult(ERROR, 1014, "Excluded minimum is '%s', found '%s'.");
  private static final ValidationResult ERR = new ValidationResult(ERROR, 1015, "Minimum is '%s', found '%s'.");

  private static final ValidationResults.CrumbInfo CRUMB_INFO = new ValidationResults.CrumbInfo(MINIMUM, true);

  private final BigDecimal minimum;
  private final boolean excludeEqual;

  MinimumValidator(final ValidationContext<OAI3> context, final JsonNode schemaNode, final JsonNode schemaParentNode, final SchemaValidator parentSchema) {
    super(context, schemaNode, schemaParentNode, parentSchema);

    minimum = schemaNode.isNumber() ? schemaNode.decimalValue() : null;

    JsonNode exclusiveMaximumNode = schemaParentNode.get(EXCLUSIVEMINIMUM);
    if (exclusiveMaximumNode != null && exclusiveMaximumNode.isBoolean()) {
      excludeEqual = exclusiveMaximumNode.booleanValue();
    } else {
      excludeEqual = false;
    }
  }

  @Override
  public boolean validate(final JsonNode valueNode, final ValidationData<?> validation) {
    if (minimum == null || !valueNode.isNumber()) {
      return false;
    }

    final BigDecimal value = valueNode.decimalValue();
    final int compResult = value.compareTo(minimum);
    if (excludeEqual && compResult == 0) {
      validation.add(CRUMB_INFO, EXCLUSIVE_ERR, minimum, value);
    } else if (compResult < 0) {
      validation.add(CRUMB_INFO, ERR, minimum, value);
    }

    return false;
  }
}
