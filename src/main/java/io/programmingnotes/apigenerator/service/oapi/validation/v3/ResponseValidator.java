package io.programmingnotes.apigenerator.service.oapi.validation.v3;



import io.programmingnotes.apigenerator.data.oapi.v3.OpenApi3;
import io.programmingnotes.apigenerator.data.oapi.v3.Response;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationContext;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResults;
import io.programmingnotes.apigenerator.service.oapi.validation.Validator;

import static io.programmingnotes.apigenerator.service.oapi.validation.v3.OAI3Keywords.*;

class ResponseValidator extends Validator3Base<OpenApi3, Response> {
  private static final Validator<OpenApi3, Response> INSTANCE = new ResponseValidator();

  private ResponseValidator() {
  }

  public static Validator<OpenApi3, Response> instance() {
    return INSTANCE;
  }

  @Override
  public void validate(ValidationContext<OpenApi3> context, OpenApi3 api, Response response, ValidationResults results) {
    if (response.isRef()) {
      validateReference(context, api, response, results, CRUMB_$REF, ResponseValidator.instance(), Response.class);
    } else {
      validateRequired(response.getDescription(), results, true, CRUMB_DESCRIPTION);
      validateMap(context, api, response.getHeaders(), results, false, CRUMB_HEADERS, null, HeaderValidator.instance());
      validateMap(context, api, response.getContentMediaTypes(), results, false, CRUMB_CONTENT, Regexes.NOEXT_REGEX, MediaTypeValidator.instance());
      validateMap(context, api, response.getLinks(), results, false, CRUMB_LINKS, Regexes.NOEXT_NAME_REGEX, LinkValidator.instance());
      validateMap(context, api, response.getExtensions(), results, false, CRUMB_EXTENSIONS, Regexes.EXT_REGEX, null);
    }
  }
}
