package io.programmingnotes.apigenerator.service.oapi.validation.v3;



import io.programmingnotes.apigenerator.data.oapi.v3.Discriminator;
import io.programmingnotes.apigenerator.data.oapi.v3.OpenApi3;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationContext;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResults;
import io.programmingnotes.apigenerator.service.oapi.validation.Validator;

import static io.programmingnotes.apigenerator.service.oapi.validation.v3.OAI3Keywords.CRUMB_PROPERTYNAME;


class DiscriminatorValidator extends Validator3Base<OpenApi3, Discriminator> {
  private static final Validator<OpenApi3, Discriminator> INSTANCE = new DiscriminatorValidator();

  private DiscriminatorValidator() {
  }

  public static Validator<OpenApi3, Discriminator> instance() {
    return INSTANCE;
  }

  @Override
  public void validate(ValidationContext<OpenApi3> context, OpenApi3 api, Discriminator discriminator, ValidationResults results) {
    // mapping references are checked in parsing phase.
    validateString(discriminator.getPropertyName(), results, true, CRUMB_PROPERTYNAME);
  }
}
