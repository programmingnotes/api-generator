package io.programmingnotes.apigenerator.service.oapi.schema.validator.v3;

import com.fasterxml.jackson.databind.JsonNode;
import io.programmingnotes.apigenerator.data.oapi.v3.OAI3;
import io.programmingnotes.apigenerator.data.oapi.v3.OAI3SchemaKeywords;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.BaseJsonValidator;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.ValidationContext;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.ValidationData;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResult;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResults;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.programmingnotes.apigenerator.data.oapi.v3.OAI3SchemaKeywords.ADDITIONALPROPERTIES;
import static io.programmingnotes.apigenerator.service.oapi.validation.ValidationSeverity.ERROR;


/**
 * additionalProperties keyword validator.
 * <p/>
 * <a href="https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.2.md#schemaObject" />
 * <p/>
 * <a href="https://tools.ietf.org/html/draft-wright-json-schema-validation-00#page-10" />
 */
class AdditionalPropertiesValidator extends BaseJsonValidator<OAI3> {
  private static final ValidationResult ERR = new ValidationResult(ERROR, 1000, "Additional property '%s' is not allowed.");

  private static final ValidationResults.CrumbInfo CRUMB_INFO = new ValidationResults.CrumbInfo(ADDITIONALPROPERTIES, true);

  private final Set<String> allowedProperties;
  private final Set<Pattern> allowedPatternProperties;
  private final Boolean additionalPropertiesAllowed;
  private final SchemaValidator additionalPropertiesSchema;

  AdditionalPropertiesValidator(final ValidationContext<OAI3> context, final JsonNode schemaNode, final JsonNode schemaParentNode, final SchemaValidator parentSchema) {
    super(context, schemaNode, schemaParentNode, parentSchema);

    if (schemaNode.isBoolean()) {
      additionalPropertiesAllowed = schemaNode.booleanValue();
      additionalPropertiesSchema = null;
    } else /*if (schemaNode.isObject())*/ {
      additionalPropertiesAllowed = false;
      additionalPropertiesSchema = new SchemaValidator(context, CRUMB_INFO, schemaNode, schemaParentNode, parentSchema);
    }

    if (Boolean.TRUE.equals(additionalPropertiesAllowed)) {
      allowedProperties = null;
      allowedPatternProperties = null;

    } else {
      JsonNode propertiesNode = schemaParentNode.get(OAI3SchemaKeywords.PROPERTIES);
      allowedProperties = setupAllowedProperties(propertiesNode);

      JsonNode patternPropertiesNode = schemaParentNode.get(OAI3SchemaKeywords.PATTERNPROPERTIES);
      allowedPatternProperties = setupAllowedPatternProperties(patternPropertiesNode);
    }
  }

  @Override
  public boolean validate(final JsonNode valueNode, final ValidationData<?> validation) {
    if (Boolean.TRUE.equals(additionalPropertiesAllowed)) return false;

    validate(() -> {
      for (Iterator<String> it = valueNode.fieldNames(); it.hasNext(); ) {
        String fieldName = it.next();

        if (!checkAgainstPatternProperties(fieldName) && !checkAgainstProperties(fieldName)) {
          if (additionalPropertiesSchema != null) {
            additionalPropertiesSchema.validateWithContext(valueNode.get(fieldName), validation);
          } else {
            validation.add(CRUMB_INFO, ERR, fieldName);
          }
        }
      }
    });

    return false;
  }

  private Set<String> setupAllowedProperties(JsonNode propertiesNode) {
    Set<String> values;

    if (propertiesNode != null) {
      values = new HashSet<>();
      for (Iterator<String> it = propertiesNode.fieldNames(); it.hasNext(); ) {
        values.add(it.next());
      }
    } else {
      values = null;
    }

    return values;
  }

  private Set<Pattern> setupAllowedPatternProperties(JsonNode patternPropertiesNode) {
    Set<Pattern> values;

    if (patternPropertiesNode != null) {
      values = new HashSet<>();
      for (Iterator<String> it = patternPropertiesNode.fieldNames(); it.hasNext(); ) {
        values.add(Pattern.compile(it.next()));
      }
    } else {
      values = null;
    }

    return values;
  }

  private boolean checkAgainstPatternProperties(final String fieldName) {
    if (allowedPatternProperties != null) {
      for (Pattern pattern : allowedPatternProperties) {
        Matcher m = pattern.matcher(fieldName);
        if (m.find()) {
          return true;
        }
      }
    }

    return false;
  }

  private boolean checkAgainstProperties(final String fieldName) {
    return allowedProperties != null && allowedProperties.contains(fieldName);
  }
}
