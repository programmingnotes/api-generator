package io.programmingnotes.apigenerator.service.oapi.validation.v3;



import io.programmingnotes.apigenerator.data.oapi.v3.Info;
import io.programmingnotes.apigenerator.data.oapi.v3.OpenApi3;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationContext;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResults;
import io.programmingnotes.apigenerator.service.oapi.validation.Validator;

import static io.programmingnotes.apigenerator.service.oapi.validation.v3.OAI3Keywords.*;

class InfoValidator extends Validator3Base<OpenApi3, Info> {
  private static final Validator<OpenApi3, Info> INSTANCE = new InfoValidator();

  private InfoValidator() {
  }

  public static Validator<OpenApi3, Info> instance() {
    return INSTANCE;
  }

  @Override
  public void validate(ValidationContext<OpenApi3> context, OpenApi3 api, Info info, ValidationResults results) {
    // VALIDATION EXCLUSIONS :
    // description
    validateField(context, api, info.getContact(), results, false, CRUMB_CONTACT, ContactValidator.instance());
    validateMap(context, api, info.getExtensions(), results, false, CRUMB_EXTENSIONS, Regexes.EXT_REGEX, null);
    validateField(context, api, info.getLicense(), results, false, CRUMB_LICENSE, LicenseValidator.instance());
    validateUrl(api, info.getTermsOfService(), results, false, CRUMB_TERMSOFSERVICE);
    validateString(info.getTitle(), results, true, CRUMB_TITLE);
    validateString(info.getVersion(), results, true, CRUMB_VERSION);
  }
}
