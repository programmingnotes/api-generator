package io.programmingnotes.apigenerator.service.oapi.validation.v3;



import io.programmingnotes.apigenerator.data.oapi.v3.OpenApi3;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationContext;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResult;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResults;
import io.programmingnotes.apigenerator.data.oapi.v3.Header;
import io.programmingnotes.apigenerator.service.oapi.validation.Validator;

import java.util.regex.Pattern;

import static io.programmingnotes.apigenerator.service.oapi.validation.ValidationSeverity.ERROR;
import static io.programmingnotes.apigenerator.service.oapi.validation.v3.OAI3Keywords.*;

class HeaderValidator extends Validator3Base<OpenApi3, Header> {
  private static final ValidationResult CONTENT_ONY_ONE_ERR = new ValidationResult(ERROR, 113, "Content can only contain one media type.");
  private static final ValidationResult CONTENT_SCHEMA_EXCLUSIVE_ERR = new ValidationResult(ERROR, 114, "Content and schema are mutually exclusive.");

  private static final Validator<OpenApi3, Header> INSTANCE = new HeaderValidator();

  private HeaderValidator() {
  }

  public static Validator<OpenApi3, Header> instance() {
    return INSTANCE;
  }

  @Override
  public void validate(ValidationContext<OpenApi3> context, OpenApi3 api, Header header, ValidationResults results) {
    // VALIDATION EXCLUSIONS :
    // allowReserved, deprecated, description, example, examples, explode, required
    if (header.isRef()) {
      validateReference(context, api, header, results, CRUMB_$REF, HeaderValidator.instance(), Header.class);
    } else {
      validateString(header.getStyle(), results, false, Pattern.compile("simple"), CRUMB_STYLE); // Only simple is allowed.
      validateField(context, api, header.getSchema(), results, false, CRUMB_SCHEMA, SchemaValidator.instance());
      validateMap(context, api, header.getContentMediaTypes(), results, false, CRUMB_CONTENT, Regexes.NOEXT_REGEX, MediaTypeValidator.instance());
      validateMap(context, api, header.getExtensions(), results, false, CRUMB_EXTENSIONS, Regexes.EXT_REGEX, null);

      // Content or schema, not both
      if (header.getContentMediaTypes() != null && header.getSchema() != null) {
        results.add(CONTENT_SCHEMA_EXCLUSIVE_ERR);
      }
      // only one content type
      if (header.getContentMediaTypes() != null && header.getContentMediaTypes().size() > 1) {
        results.add(CONTENT_ONY_ONE_ERR);
      }
    }
  }
}
