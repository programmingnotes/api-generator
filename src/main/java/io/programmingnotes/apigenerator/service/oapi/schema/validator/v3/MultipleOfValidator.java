package io.programmingnotes.apigenerator.service.oapi.schema.validator.v3;

import com.fasterxml.jackson.databind.JsonNode;
import io.programmingnotes.apigenerator.data.oapi.v3.OAI3;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.BaseJsonValidator;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.ValidationContext;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.ValidationData;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResult;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResults;


import java.math.BigDecimal;

import static io.programmingnotes.apigenerator.data.oapi.v3.OAI3SchemaKeywords.MULTIPLEOF;
import static io.programmingnotes.apigenerator.service.oapi.validation.ValidationSeverity.ERROR;


/**
 * multipleOf keyword validator.
 * <p/>
 * <a href="https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.2.md#schemaObject" />
 * <p/>
 * <a href="https://tools.ietf.org/html/draft-wright-json-schema-validation-00#page-6" />
 * <p/>
 * The value of "multipleOf" MUST be a number, strictly greater than 0.
 * <p>
 * A numeric instance is only valid if division by this keyword's value
 * results in an integer.
 */
class MultipleOfValidator extends BaseJsonValidator<OAI3> {
  private static final ValidationResult ERR = new ValidationResult(ERROR, 1019, "Value '%s' is not a multiple of '%s'.");

  private static final ValidationResults.CrumbInfo CRUMB_INFO = new ValidationResults.CrumbInfo(MULTIPLEOF, true);

  private static final BigDecimal DIVISIBLE = BigDecimal.valueOf(0.0);
  private final BigDecimal multiple;

  MultipleOfValidator(final ValidationContext<OAI3> context, final JsonNode schemaNode, final JsonNode schemaParentNode, final SchemaValidator parentSchema) {
    super(context, schemaNode, schemaParentNode, parentSchema);

    multiple
      = (schemaNode.isNumber() && schemaNode.decimalValue().compareTo(DIVISIBLE) > 0)
      ? schemaNode.decimalValue()
      : null;
  }

  @Override
  public boolean validate(final JsonNode valueNode, final ValidationData<?> validation) {
    if (multiple == null || !valueNode.isNumber()) {
      return false;
    }

    BigDecimal value = valueNode.decimalValue();
    BigDecimal remainder = value.remainder(multiple);
    if (remainder.compareTo(DIVISIBLE) != 0) {
      validation.add(CRUMB_INFO, ERR, value, multiple);
    }

    return false;
  }
}
