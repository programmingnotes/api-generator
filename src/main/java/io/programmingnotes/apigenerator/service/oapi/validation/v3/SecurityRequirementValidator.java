package io.programmingnotes.apigenerator.service.oapi.validation.v3;



import io.programmingnotes.apigenerator.data.oapi.v3.OpenApi3;
import io.programmingnotes.apigenerator.data.oapi.v3.SecurityRequirement;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationContext;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResult;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResults;
import io.programmingnotes.apigenerator.service.oapi.validation.Validator;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.programmingnotes.apigenerator.service.oapi.validation.ValidationSeverity.ERROR;
import static io.programmingnotes.apigenerator.service.oapi.validation.v3.OAI3Keywords.OAUTH2;
import static io.programmingnotes.apigenerator.service.oapi.validation.v3.OAI3Keywords.OPENIDCONNECT;

class SecurityRequirementValidator extends Validator3Base<OpenApi3, SecurityRequirement> {
  private static final ValidationResult SCHEME_NOT_DEFINED = new ValidationResult(ERROR, 139, "Security scheme '%s' is not defined in components");
  private static final ValidationResult SEC_REQ_NOT_ALLOWED = new ValidationResult(ERROR, 140, "Security requirement parameters in '%s' not allowed with scheme type '%s'");

  private static final Validator<OpenApi3, SecurityRequirement> INSTANCE = new SecurityRequirementValidator();

  private SecurityRequirementValidator() {
  }

  public static Validator<OpenApi3, SecurityRequirement> instance() {
    return INSTANCE;
  }

  @Override
  public void validate(ValidationContext<OpenApi3> context, OpenApi3 api, SecurityRequirement securityRequirement, ValidationResults results) {
    Set<String> schemes = api.getComponents().getSecuritySchemes().keySet();

    for (Map.Entry<String, List<String>> entry : securityRequirement.getRequirements().entrySet()) {
      if (!schemes.contains(entry.getKey())) {
        results.add(SCHEME_NOT_DEFINED, entry.getKey());
      } else {
        String type = api.getComponents().getSecurityScheme(entry.getKey()).getType();

        if (type == null || OAUTH2.equals(type) || OPENIDCONNECT.equals(type)) {
          continue;
        }

        if (!entry.getValue().isEmpty()) {
          results.add(SEC_REQ_NOT_ALLOWED, entry.getKey(), type);
        }
      }
    }
  }
}
