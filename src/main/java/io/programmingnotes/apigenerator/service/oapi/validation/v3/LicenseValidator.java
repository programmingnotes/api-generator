package io.programmingnotes.apigenerator.service.oapi.validation.v3;



import io.programmingnotes.apigenerator.data.oapi.v3.License;
import io.programmingnotes.apigenerator.data.oapi.v3.OpenApi3;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationContext;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResults;
import io.programmingnotes.apigenerator.service.oapi.validation.Validator;

import static io.programmingnotes.apigenerator.service.oapi.validation.v3.OAI3Keywords.*;

class LicenseValidator extends Validator3Base<OpenApi3, License> {
  private static final Validator<OpenApi3, License> INSTANCE = new LicenseValidator();

  private LicenseValidator() {
  }

  public static Validator<OpenApi3, License> instance() {
    return INSTANCE;
  }

  @Override
  public void validate(ValidationContext<OpenApi3> context, OpenApi3 api, License license, ValidationResults results) {
    validateMap(context, api, license.getExtensions(), results, false, CRUMB_EXTENSIONS, Regexes.EXT_REGEX, null);
    validateString(license.getName(), results, true, CRUMB_NAME);
    validateUrl(api, license.getUrl(), results, false, CRUMB_URL);
  }
}
