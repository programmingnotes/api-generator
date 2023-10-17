package io.programmingnotes.apigenerator.service.oapi.validation.v3;


import io.programmingnotes.apigenerator.data.oapi.v3.OpenApi3;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationContext;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationException;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResults;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationSeverity;

public class OpenApi3Validator {
  private static final String VALIDATION_FAILURE = "OpenApi3 validation failure";

  private static final OpenApi3Validator INSTANCE = new OpenApi3Validator();

  private OpenApi3Validator() {
  }

  public static OpenApi3Validator instance() {
    return INSTANCE;
  }

  public ValidationResults validate(OpenApi3 api) throws ValidationException {
    final ValidationContext<OpenApi3> context = new ValidationContext<>();
    final ValidationResults results = new ValidationResults();

    context.validate(api, api, OpenApiValidator.instance(), results);

    if (results.severity() == ValidationSeverity.ERROR) {
      throw new ValidationException(VALIDATION_FAILURE, results);
    }

    return results;
  }
}
