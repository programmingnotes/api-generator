package io.programmingnotes.apigenerator.service.oapi.validation.v3;



import io.programmingnotes.apigenerator.data.oapi.v3.ExternalDocs;
import io.programmingnotes.apigenerator.data.oapi.v3.OpenApi3;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationContext;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResults;
import io.programmingnotes.apigenerator.service.oapi.validation.Validator;

import static io.programmingnotes.apigenerator.service.oapi.validation.v3.OAI3Keywords.CRUMB_EXTENSIONS;
import static io.programmingnotes.apigenerator.service.oapi.validation.v3.OAI3Keywords.CRUMB_EXTERNALDOCS;

class ExternalDocsValidator extends Validator3Base<OpenApi3, ExternalDocs> {
  private static final Validator<OpenApi3, ExternalDocs> INSTANCE = new ExternalDocsValidator();

  private ExternalDocsValidator() {
  }

  public static Validator<OpenApi3, ExternalDocs> instance() {
    return INSTANCE;
  }

  @Override
  public void validate(ValidationContext<OpenApi3> context, OpenApi3 api, ExternalDocs externalDocs, ValidationResults results) {
    // VALIDATION EXCLUSIONS :
    // description
    validateMap(context, api, externalDocs.getExtensions(), results, false, CRUMB_EXTENSIONS, Regexes.EXT_REGEX, null);
    validateUrl(api, externalDocs.getUrl(), results, true, CRUMB_EXTERNALDOCS);
  }
}
