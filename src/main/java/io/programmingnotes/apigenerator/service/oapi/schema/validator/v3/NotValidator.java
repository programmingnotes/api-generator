package io.programmingnotes.apigenerator.service.oapi.schema.validator.v3;

import com.fasterxml.jackson.databind.JsonNode;
import io.programmingnotes.apigenerator.data.oapi.v3.OAI3;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.BaseJsonValidator;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.ValidationContext;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.ValidationData;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationException;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResult;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResults;

import static io.programmingnotes.apigenerator.data.oapi.v3.OAI3SchemaKeywords.NOT;
import static io.programmingnotes.apigenerator.service.oapi.validation.ValidationSeverity.ERROR;


/**
 * not keyword validator.
 * <p/>
 * <a href="https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.2.md#schemaObject" />
 * <p/>
 * <a href="https://tools.ietf.org/html/draft-wright-json-schema-validation-00#page-12" />
 */
class NotValidator extends BaseJsonValidator<OAI3> {
  private static final ValidationResult ERR = new ValidationResult(ERROR, 1020, "Schema should not be valid.");

  private static final ValidationResults.CrumbInfo CRUMB_INFO = new ValidationResults.CrumbInfo(NOT, true);

  private final SchemaValidator schema;

  NotValidator(final ValidationContext<OAI3> context, final JsonNode schemaNode, final JsonNode schemaParentNode, final SchemaValidator parentSchema) {
    super(context, schemaNode, schemaParentNode, parentSchema);

    schema = new SchemaValidator(context, CRUMB_INFO, schemaNode, schemaParentNode, parentSchema);
  }

  @Override
  public boolean validate(final JsonNode valueNode, final ValidationData<?> validation) {
    try {
      schema.validate(valueNode);
      validation.add(CRUMB_INFO, ERR);
    } catch (ValidationException ex) {
      // Succeed case
    }

    return false;
  }
}
