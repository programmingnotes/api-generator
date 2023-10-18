package io.programmingnotes.apigenerator.service.oapi.validation.v3;



import io.programmingnotes.apigenerator.data.oapi.v3.OpenApi3;
import io.programmingnotes.apigenerator.data.oapi.v3.ServerVariable;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationContext;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResults;
import io.programmingnotes.apigenerator.service.oapi.validation.Validator;

import static io.programmingnotes.apigenerator.service.oapi.validation.v3.OAI3Keywords.*;

class ServerVariableValidator extends Validator3Base<OpenApi3, ServerVariable> {
  private static final Validator<OpenApi3, ServerVariable> INSTANCE = new ServerVariableValidator();

  private ServerVariableValidator() {
  }

  public static Validator<OpenApi3, ServerVariable> instance() {
    return INSTANCE;
  }

  @Override
  public void validate(ValidationContext<OpenApi3> context, OpenApi3 api, final ServerVariable variable, final ValidationResults results) {
    validateList(context, api, variable.getEnums(), results, false, 1, CRUMB_ENUM, null);
    validateString(variable.getDefault(), results, true, CRUMB_DEFAULT);
    validateString(variable.getDescription(), results, false, CRUMB_DESCRIPTION);
  }
}
