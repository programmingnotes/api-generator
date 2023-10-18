package io.programmingnotes.apigenerator.service.oapi.validation.v3;



import io.programmingnotes.apigenerator.data.oapi.v3.OpenApi3;
import io.programmingnotes.apigenerator.data.oapi.v3.Tag;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationContext;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResults;
import io.programmingnotes.apigenerator.service.oapi.validation.Validator;

import static io.programmingnotes.apigenerator.service.oapi.validation.v3.OAI3Keywords.*;

class TagValidator extends Validator3Base<OpenApi3, Tag> {
  private static final Validator<OpenApi3, Tag> INSTANCE = new TagValidator();

  private TagValidator() {
  }

  public static Validator<OpenApi3, Tag> instance() {
    return INSTANCE;
  }

  @Override
  public void validate(ValidationContext<OpenApi3> context, OpenApi3 api, Tag tag, ValidationResults results) {
    validateString(tag.getName(), results, true, CRUMB_NAME);
    validateField(context, api, tag.getExternalDocs(), results, false, CRUMB_EXTERNALDOCS, ExternalDocsValidator.instance());
    validateMap(context, api, tag.getExtensions(), results, false, CRUMB_EXTENSIONS, Regexes.EXT_REGEX, null);
  }
}
