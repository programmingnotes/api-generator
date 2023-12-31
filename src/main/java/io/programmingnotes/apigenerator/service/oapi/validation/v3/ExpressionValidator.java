package io.programmingnotes.apigenerator.service.oapi.validation.v3;

import io.programmingnotes.apigenerator.data.oapi.OAIContext;
import io.programmingnotes.apigenerator.data.oapi.v3.*;
import io.programmingnotes.apigenerator.exception.DecodeException;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResult;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResults;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.programmingnotes.apigenerator.data.oapi.v3.OAI3SchemaKeywords.TYPE_ARRAY;
import static io.programmingnotes.apigenerator.service.oapi.validation.ValidationSeverity.ERROR;

;

abstract class ExpressionValidator<M> extends Validator3Base<OpenApi3, M> {
  private static final ValidationResult PARAM_NOT_FOUND_ERR = new ValidationResult(ERROR, 111, "Parameter '%s' not found in operation.");
  private static final ValidationResult PARAM_PATH_EXCEPTION_ERR = new ValidationResult(ERROR, 112, "Path '%s' is malformed.\n'%s'");

  private static final Pattern PARAM_PATTERN = Pattern.compile("\\{(.*?)}");
  private static final Pattern PATTERN_REQUEST_PARAM = Pattern.compile("^(\\$request)(?:\\.)(query(?=\\.)|path(?=\\.)|header(?=\\.)|body(?=#/))(?:\\.|#/)(.+)");
  private static final Pattern PATTERN_RESPONSE_PARAM = Pattern.compile("^(\\$response)(?:\\.)(header(?=\\.)|body(?=#/))(?:\\.|#/)(.+)");

  void validateExpression(OpenApi3 api, Operation operation, String expression, ValidationResults results) {
    // Check against expression fragments
    boolean paramFound = false;
    Matcher matcher = PARAM_PATTERN.matcher(expression);
    while (matcher.find()) {
      paramFound = true;
      if (!checkRequestParameter(api, operation, matcher.group(1), results)) {
        checkResponseParameter(api, operation, matcher.group(1), results);
      }
    }

    // Check against full expression
    if (!paramFound && !checkRequestParameter(api, operation, expression, results)) {
      checkResponseParameter(api, operation, expression, results);
    }
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  private boolean checkRequestParameter(OpenApi3 api, Operation operation, String propValue, ValidationResults results) {
    Matcher matcher = PATTERN_REQUEST_PARAM.matcher(propValue);
    boolean matches = matcher.matches();

    // group 1 : $request, group 2 : in, group 3 : value (JSON pointer for body)
    if (matches) {
      if (matcher.group(2).equals("body")) {
        RequestBody reqBody = operation.getRequestBody();
        if (reqBody != null && hasBodyProperty(api, matcher.group(3), reqBody.getContentMediaTypes(), results)) {
          return true;
        }

        results.add(PARAM_NOT_FOUND_ERR, propValue);
      } else {
        if (checkParameterIn(matcher.group(2), matcher.group(3), api.getContext(), operation, results)) {
          return true;
        }
      }
    }

    return matches;
  }

  @SuppressWarnings("UnusedReturnValue")
  private boolean checkResponseParameter(OpenApi3 api, Operation operation, String propValue, ValidationResults results) {
    if (operation.getResponses() == null) return false;

    Matcher matcher = PATTERN_RESPONSE_PARAM.matcher(propValue);
    boolean matches = matcher.matches();

    // group 1 : $request, group 2 : in, group 3 : value (JSON pointer for body)
    if (matches) {
      if (matcher.group(2).equals("body")) {
        for (Response response : operation.getResponses().values()) {
          if (hasBodyProperty(api, matcher.group(3), response.getContentMediaTypes(), results)) {
            return true;
          }
        }

        results.add(PARAM_NOT_FOUND_ERR, propValue);
      } else {
        for (Response response : operation.getResponses().values()) {
          if (response.getHeaders() != null) {
            for (String header : response.getHeaders().keySet()) {
              if (header.equalsIgnoreCase(matcher.group(3))) {
                return true;
              }
            }
          }
        }
        results.add(PARAM_NOT_FOUND_ERR, propValue);
        return false;
      }
    }

    return matches;
  }

  private boolean hasBodyProperty(OpenApi3 api, String propValue, Map<String, MediaType> contentMediaTypes, ValidationResults results) {
    if (contentMediaTypes == null) return false;

    String[] pathFragments = propValue.split("/");
    for (Map.Entry<String, MediaType> entry : contentMediaTypes.entrySet()) {
      try {
        if (hasBodyProperty(api, entry.getValue().getSchema(), pathFragments, 0)) {
          return true;
        }
      } catch (DecodeException e) {
        results.add(PARAM_PATH_EXCEPTION_ERR, propValue, e.getMessage());
      }
    }

    return false;
  }

  private boolean hasBodyProperty(OpenApi3 api, Schema schema, String[] pathFragments, int index) throws DecodeException {
    if (schema == null) {
      return false;
    }

    if (pathFragments.length > index) {
      if (schema.isRef()) {
        schema = schema.getReference(api.getContext()).getMappedContent(Schema.class);
      }

      if (TYPE_ARRAY.equals(schema.getType())) {
        return hasBodyProperty(api, schema.getItemsSchema(), pathFragments, index);
      }

      Schema subSchema = schema.getProperty(pathFragments[index]);
      if (subSchema == null) {
        return false;
      }

      index++;
      return (pathFragments.length == index) || hasBodyProperty(api, subSchema, pathFragments, index);
    }

    return false;
  }

  private boolean checkParameterIn(String in,
                                   String propName,
                                   OAIContext context,
                                   Operation operation,
                                   ValidationResults results) {

    for (Parameter param : operation.getParametersIn(context, in)) {
      if (param.getName().equals(propName)) {
        return true;
      }
    }
    results.add(PARAM_NOT_FOUND_ERR, propName);
    return false;
  }
}
