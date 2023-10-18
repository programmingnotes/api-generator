package io.programmingnotes.apigenerator.service.oapi.validation.v3;



import io.programmingnotes.apigenerator.data.oapi.v3.Callback;
import io.programmingnotes.apigenerator.data.oapi.v3.OpenApi3;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationContext;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResults;

import static io.programmingnotes.apigenerator.service.oapi.validation.v3.OAI3Keywords.CRUMB_$REF;
import static io.programmingnotes.apigenerator.service.oapi.validation.v3.OAI3Keywords.CRUMB_EXTENSIONS;

class CallbackValidator extends ExpressionValidator<Callback> {
  private static final CallbackValidator INSTANCE = new CallbackValidator();

  private CallbackValidator() {
  }

  public static CallbackValidator instance() {
    return INSTANCE;
  }

  @Override
  public void validate(ValidationContext<OpenApi3> context, OpenApi3 api, Callback callback, ValidationResults results) {
    if (callback.isRef()) {
      validateReference(context, api, callback, results, CRUMB_$REF, CallbackValidator.instance(), Callback.class);
    } else {
      validateMap(context, api, callback.getCallbackPaths(), results, false, null, Regexes.NOEXT_REGEX, PathValidator.instance());
      validateMap(context, api, callback.getExtensions(), results, false, CRUMB_EXTENSIONS, Regexes.EXT_REGEX, null);
    }
  }
}
