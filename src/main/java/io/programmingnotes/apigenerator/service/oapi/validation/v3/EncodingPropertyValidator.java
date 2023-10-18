package io.programmingnotes.apigenerator.service.oapi.validation.v3;



import io.programmingnotes.apigenerator.data.oapi.v3.EncodingProperty;
import io.programmingnotes.apigenerator.data.oapi.v3.OpenApi3;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationContext;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResults;
import io.programmingnotes.apigenerator.service.oapi.validation.Validator;

import static io.programmingnotes.apigenerator.service.oapi.validation.v3.OAI3Keywords.*;

class EncodingPropertyValidator extends Validator3Base<OpenApi3, EncodingProperty> {
  private static final Validator<OpenApi3, EncodingProperty> INSTANCE = new EncodingPropertyValidator();

  private EncodingPropertyValidator() {
  }

  public static Validator<OpenApi3, EncodingProperty> instance() {
    return INSTANCE;
  }

  @Override
  public void validate(ValidationContext<OpenApi3> context, OpenApi3 api, EncodingProperty encodingProperty, ValidationResults results) {
    // VALIDATION EXCLUSIONS :
    // explode
    validateString(encodingProperty.getContentType(), results, false, CRUMB_CONTENTTYPE);
    validateMap(context, api, encodingProperty.getExtensions(), results, false, CRUMB_EXTENSIONS, Regexes.EXT_REGEX, null);
    validateMap(context, api, encodingProperty.getHeaders(), results, false, CRUMB_HEADERS, Regexes.NOEXT_REGEX, null);
    validateString(encodingProperty.getStyle(), results, false, Regexes.STYLE_REGEX, CRUMB_STYLE);
  }
}
