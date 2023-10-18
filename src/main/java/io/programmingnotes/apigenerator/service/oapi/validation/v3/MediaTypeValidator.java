package io.programmingnotes.apigenerator.service.oapi.validation.v3;



import io.programmingnotes.apigenerator.data.oapi.v3.EncodingProperty;
import io.programmingnotes.apigenerator.data.oapi.v3.MediaType;
import io.programmingnotes.apigenerator.data.oapi.v3.OpenApi3;
import io.programmingnotes.apigenerator.data.oapi.v3.Schema;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationContext;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResult;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResults;
import io.programmingnotes.apigenerator.service.oapi.validation.Validator;

import java.util.Map;
import java.util.Set;

import static io.programmingnotes.apigenerator.service.oapi.validation.ValidationSeverity.ERROR;
import static io.programmingnotes.apigenerator.service.oapi.validation.v3.OAI3Keywords.*;

class MediaTypeValidator extends Validator3Base<OpenApi3, MediaType> {
  private static final ValidationResult ENCODING_MISMATCH = new ValidationResult(ERROR, 119, "Encoding property '%s' is not a corresponding schema property");

  private static final Validator<OpenApi3, MediaType> INSTANCE = new MediaTypeValidator();

  private MediaTypeValidator() {
  }

  public static Validator<OpenApi3, MediaType> instance() {
    return INSTANCE;
  }

  @Override
  public void validate(ValidationContext<OpenApi3> context, OpenApi3 api, MediaType mediaType, ValidationResults results) {
    // VALIDATION EXCLUSIONS :
    // example, examples
    validateMap(context, api, mediaType.getEncodings(), results, false, CRUMB_ENCODING, Regexes.NOEXT_NAME_REGEX, EncodingPropertyValidator.instance());
    validateMap(context, api, mediaType.getExtensions(), results, false, CRUMB_EXTENSIONS, Regexes.EXT_REGEX, null);
    validateField(context, api, mediaType.getSchema(), results, false, CRUMB_SCHEMA, SchemaValidator.instance());
    checkEncodingProperties(mediaType, results);
  }

  private void checkEncodingProperties(MediaType mediaType, ValidationResults results) {
    Schema schema = mediaType.getSchema();
    if (schema == null) {
      return;
    }

    Map<String, Schema> schemaProps = schema.getProperties();
    Map<String, EncodingProperty> mediaEncodings = mediaType.getEncodings();

    if (schemaProps != null && mediaEncodings != null) {
      Set<String> propNames = schemaProps.keySet();
      for (String encodingPropertyName : mediaEncodings.keySet()) {
        if (!propNames.contains(encodingPropertyName)) {
          results.add(CRUMB_ENCODING, ENCODING_MISMATCH, encodingPropertyName);
        }
      }
    }
  }
}
