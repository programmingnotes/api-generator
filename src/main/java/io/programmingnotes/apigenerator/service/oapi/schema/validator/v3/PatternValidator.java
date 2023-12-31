package io.programmingnotes.apigenerator.service.oapi.schema.validator.v3;

import com.fasterxml.jackson.databind.JsonNode;
import io.programmingnotes.apigenerator.data.oapi.v3.OAI3;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.BaseJsonValidator;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.ValidationContext;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.ValidationData;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResult;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResults;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.programmingnotes.apigenerator.data.oapi.v3.OAI3SchemaKeywords.PATTERN;
import static io.programmingnotes.apigenerator.service.oapi.validation.ValidationSeverity.ERROR;


/**
 * pattern keyword validator.
 * <p/>
 * <a href="https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.2.md#schemaObject" />
 * <p/>
 * <a href="https://tools.ietf.org/html/draft-wright-json-schema-validation-00#page-7" />
 */
class PatternValidator extends BaseJsonValidator<OAI3> {
  private static final ValidationResult PATTERN_DEF_ERR = new ValidationResult(ERROR, 1024, "Wrong pattern definition '%s'.");
  private static final ValidationResult ERR = new ValidationResult(ERROR, 1025, "'%s' does not respect pattern '%s'.");

  private static final ValidationResults.CrumbInfo CRUMB_INFO = new ValidationResults.CrumbInfo(PATTERN, true);

  private final String patternStr;
  private final Pattern pattern;

  PatternValidator(final ValidationContext<OAI3> context, final JsonNode schemaNode, final JsonNode schemaParentNode, final SchemaValidator parentSchema) {
    super(context, schemaNode, schemaParentNode, parentSchema);

    patternStr = schemaNode.asText();
    pattern = schemaNode.isTextual() ? Pattern.compile(schemaNode.textValue()) : null;
  }

  @Override
  public boolean validate(final JsonNode valueNode, final ValidationData<?> validation) {
    if (pattern == null) {
      validation.add(CRUMB_INFO, PATTERN_DEF_ERR, patternStr);
      return false;
    } else if (!valueNode.isTextual()) {
      return false;
    }

    String value = valueNode.textValue();
    Matcher m = pattern.matcher(value);
    if (!m.find()) {
      validation.add(CRUMB_INFO, ERR, value, patternStr);
    }

    return false;
  }
}
