package io.programmingnotes.apigenerator.service.oapi.validation.v3;



import io.programmingnotes.apigenerator.data.oapi.reference.Reference;
import io.programmingnotes.apigenerator.data.oapi.v3.Link;
import io.programmingnotes.apigenerator.data.oapi.v3.OpenApi3;
import io.programmingnotes.apigenerator.data.oapi.v3.Operation;
import io.programmingnotes.apigenerator.data.oapi.v3.Parameter;
import io.programmingnotes.apigenerator.exception.DecodeException;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationContext;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResult;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResults;

import static io.programmingnotes.apigenerator.service.oapi.validation.ValidationSeverity.ERROR;
import static io.programmingnotes.apigenerator.service.oapi.validation.v3.OAI3Keywords.*;

class LinkValidator extends ExpressionValidator<Link> {
  private static final ValidationResult OP_FIELD_MISSING_ERR = new ValidationResult(ERROR, 115, "'operationRef', 'operationId' or '$ref' field missing.");
  private static final ValidationResult OP_FIELD_EXCLUSIVE_ERR = new ValidationResult(ERROR, 116, "'operationRef' and 'operationId' fields are mutually exclusives.");
  private static final ValidationResult OP_NOT_FOUND_ERR = new ValidationResult(ERROR, 117, "'%s' not found.");
  private static final ValidationResult PARAM_NOT_FOUND_ERR = new ValidationResult(ERROR, 118, "Parameter name '%s' not found in target operation.");

  private static final LinkValidator INSTANCE = new LinkValidator();

  private LinkValidator() {
  }

  public static LinkValidator instance() {
    return INSTANCE;
  }

  @Override
  public void validate(ValidationContext<OpenApi3> context, OpenApi3 api, Link link, ValidationResults results) {
    // VALIDATION EXCLUSIONS :
    // description
    if (link.isRef()) {
      validateReference(context, api, link, results, CRUMB_LINKS, LinkValidator.instance(), Link.class);
    } else {
      validateMap(context, api, link.getHeaders(), results, false, CRUMB_HEADERS, Regexes.NOEXT_REGEX, HeaderValidator.instance());
      validateMap(context, api, link.getExtensions(), results, false, CRUMB_EXTENSIONS, Regexes.EXT_REGEX, null);
      validateField(context, api, link.getServer(), results, false, CRUMB_SERVER, ServerValidator.instance());
    }
  }

  // This called from operation validator
  void validateWithOperation(OpenApi3 api, Operation srcOperation, Link link, ValidationResults results) {
    if (link.isRef()) {
      link = getReferenceContent(api, link, results, CRUMB_LINKS, Link.class);
    }

    String operationRef = link.getOperationRef();
    String operationId = link.getOperationId();
    Operation targetOperation = null;

    if (operationId != null && operationRef != null) {
      results.add(OP_FIELD_EXCLUSIVE_ERR);
    } else if (operationRef != null) {
      targetOperation = getOperationRefContent(api, operationRef, results);
    } else if (operationId != null) {
      targetOperation = findOperationById(api, operationId, results);
    } else {
      results.add(OP_FIELD_MISSING_ERR);
    }

    if (targetOperation != null) {
      if (link.getParameters() == null) {
        return;
      }

      // Check expressions against current operation
      for (String expression : link.getParameters().values()) {
        validateExpression(api, srcOperation, expression, results);
      }
      // Check link parameter names are available in target operation
      checkTargetOperationParameters(targetOperation, link, results);
    }
  }

  private Operation findOperationById(OpenApi3 api, String operationId, ValidationResults results) {
    Operation operation = api.getOperationById(operationId);
    if (operation == null) {
      results.add(CRUMB_OPERATIONREF, OP_NOT_FOUND_ERR, operationId);
    }

    return operation;
  }

  private void checkTargetOperationParameters(Operation operation, Link link, ValidationResults results) {
    for (String paramName : link.getParameters().keySet()) {
      boolean hasParameter = false;

      if (operation.hasParameters()) {
        for (Parameter param : operation.getParameters()) {
          if (paramName.equals(param.getName())) {
            hasParameter = true;
            break;
          }
        }
      }

      if (!hasParameter) {
        results.add(PARAM_NOT_FOUND_ERR, paramName);
      }
    }
  }

  // Why didn't they used $ref ?
  Operation getOperationRefContent(final OpenApi3 api,
                                   final String operationRef,
                                   final ValidationResults results) {

    Reference reference = api.getContext().getReferenceRegistry().getRef(operationRef);

    if (reference == null) {
      results.add(CRUMB_OPERATIONREF, REF_MISSING, operationRef);
    } else {
      try {
        return reference.getMappedContent(Operation.class);
      } catch (DecodeException e) {
        results.add(CRUMB_OPERATIONREF, REF_CONTENT_UNREADABLE, operationRef);
      }
    }

    return null;
  }
}
