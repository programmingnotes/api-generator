package io.programmingnotes.apigenerator.service.oapi.validation.v3;



import io.programmingnotes.apigenerator.data.oapi.v3.Contact;
import io.programmingnotes.apigenerator.data.oapi.v3.OpenApi3;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationContext;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResults;
import io.programmingnotes.apigenerator.service.oapi.validation.Validator;

import static io.programmingnotes.apigenerator.service.oapi.validation.v3.OAI3Keywords.*;

class ContactValidator extends Validator3Base<OpenApi3, Contact> {
  private static final Validator<OpenApi3, Contact> INSTANCE = new ContactValidator();

  private ContactValidator() {
  }

  public static Validator<OpenApi3, Contact> instance() {
    return INSTANCE;
  }

  @Override
  public void validate(ValidationContext<OpenApi3> context, OpenApi3 api, Contact contact, ValidationResults results) {
    validateString(contact.getEmail(), results, false, EMAIL_REGEX, CRUMB_EMAIL);
    validateMap(context, api, contact.getExtensions(), results, false, CRUMB_EXTENSIONS, Regexes.EXT_REGEX, null);
    validateString(contact.getName(), results, false, CRUMB_NAME);
    validateUrl(api, contact.getUrl(), results, false, CRUMB_URL);
  }
}
