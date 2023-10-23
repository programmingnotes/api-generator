package io.programmingnotes.apigenerator.service.oapi.schema.validator.v3;

import com.fasterxml.jackson.databind.JsonNode;
import io.programmingnotes.apigenerator.data.oapi.v3.OAI3;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.BaseJsonValidator;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.ValidationContext;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.ValidationData;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResult;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResults;

import static io.programmingnotes.apigenerator.data.oapi.v3.OAI3SchemaKeywords.NULLABLE;
import static io.programmingnotes.apigenerator.service.oapi.validation.ValidationSeverity.ERROR;


/**
 * nullable keyword validator.
 * <p/>
 * <a href="https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.2.md#schemaObject" />
 */
class NullableValidator extends BaseJsonValidator<OAI3> {
  private static final ValidationResult ERR = new ValidationResult(ERROR, 1021, "Null value is not allowed.");

  private static final ValidationResults.CrumbInfo CRUMB_INFO = new ValidationResults.CrumbInfo(NULLABLE, true);

  private final boolean nullable;

  NullableValidator(final ValidationContext<OAI3> context, final JsonNode schemaNode, final JsonNode schemaParentNode, final SchemaValidator parentSchema) {
    super(context, schemaNode, schemaParentNode, parentSchema);

    nullable = schemaNode.isBoolean() && schemaNode.booleanValue();
  }

  @Override
  public boolean validate(final JsonNode valueNode, final ValidationData<?> validation) {
    if (!nullable && valueNode.isNull()) {
      validation.add(CRUMB_INFO, ERR);
    }

    return false;
  }
}
