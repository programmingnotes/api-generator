package io.programmingnotes.apigenerator.service.oapi.schema.validator.v3;

import com.fasterxml.jackson.databind.JsonNode;
import io.programmingnotes.apigenerator.data.oapi.v3.OAI3;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.ValidationContext;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.ValidationData;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResult;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResults;


import java.util.ArrayList;
import java.util.List;

import static io.programmingnotes.apigenerator.data.oapi.v3.OAI3SchemaKeywords.ALLOF;
import static io.programmingnotes.apigenerator.service.oapi.validation.ValidationSeverity.ERROR;


/**
 * allOf keyword validator.
 * <p/>
 * <a href="https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.2.md#schemaObject" />
 * <p/>
 * <a href="https://tools.ietf.org/html/draft-wright-json-schema-validation-00#page-11" />
 */
class AllOfValidator extends DiscriminatorValidator {
  private static final ValidationResult ERR = new ValidationResult(ERROR, 1001, "Schema description is erroneous. allOf should have at least 1 element.");
  private static final ValidationResults.CrumbInfo CRUMB_INFO = new ValidationResults.CrumbInfo(ALLOF, true);

  AllOfValidator(final ValidationContext<OAI3> context, final JsonNode schemaNode, final JsonNode schemaParentNode, final SchemaValidator parentSchema) {
    super(context, schemaNode, schemaParentNode, parentSchema, ALLOF);
  }

  @Override
  void validateWithoutDiscriminator(final JsonNode valueNode, final ValidationData<?> validation) {
    if (validators.isEmpty()) {
      validation.add(CRUMB_INFO, ERR);
      return;
    }

    final List<ValidationResults> validResults = new ArrayList<>();

    for (SchemaValidator validator : validators) {
      ValidationData<?> schemaValidation = new ValidationData<>(validation.delegate());
      validator.validate(valueNode, schemaValidation);

      if (schemaValidation.isValid()) {
        // Collect potential results from sub validation (INFO / WARN)
        if (schemaValidation.results().size() != 0) {
          validResults.add(schemaValidation.results());
        }
      } else {
        validation.add(validation.results().crumbs(), schemaValidation.results().items(ERROR));
        return;
      }
    }

    // Report results from sub validation (INFO / WARN)
    for (ValidationResults results : validResults) {
      validation.add(validation.results().crumbs(), results);
    }
  }
}
