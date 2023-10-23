package io.programmingnotes.apigenerator.service.oapi.schema.validator.v3;

import com.fasterxml.jackson.databind.JsonNode;
import io.programmingnotes.apigenerator.data.oapi.v3.OAI3;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.BaseJsonValidator;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.ValidationContext;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.ValidationData;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResults;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * properties keyword validator.
 * <p/>
 * <a href="https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.2.md#schemaObject" />
 * <p/>
 * <a href="https://tools.ietf.org/html/draft-wright-json-schema-validation-00#page-9" />
 */
class PropertiesValidator extends BaseJsonValidator<OAI3> {
  private final Map<String, SchemaValidator> schemas;

  PropertiesValidator(final ValidationContext<OAI3> context, final JsonNode schemaNode, final JsonNode schemaParentNode, final SchemaValidator parentSchema) {
    super(context, schemaNode, schemaParentNode, parentSchema);

    schemas = new HashMap<>();
    for (Iterator<String> it = schemaNode.fieldNames(); it.hasNext(); ) {
      String pname = it.next();
      schemas.put(pname, new SchemaValidator(context, new ValidationResults.CrumbInfo(pname, false), schemaNode.get(pname), schemaParentNode, parentSchema));
    }
  }

  @Override
  public boolean validate(final JsonNode valueNode, final ValidationData<?> validation) {
    validate(() -> {
      for (Map.Entry<String, SchemaValidator> entry : schemas.entrySet()) {
        SchemaValidator propertySchema = entry.getValue();
        JsonNode propertyNode = valueNode.get(entry.getKey());

        if (propertyNode != null) {
          propertySchema.validateWithContext(propertyNode, validation);
        }
      }
    });

    return false;
  }
}
