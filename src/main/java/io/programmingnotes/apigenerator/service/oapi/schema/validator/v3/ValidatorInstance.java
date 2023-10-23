package io.programmingnotes.apigenerator.service.oapi.schema.validator.v3;

import com.fasterxml.jackson.databind.JsonNode;
import io.programmingnotes.apigenerator.data.oapi.v3.OAI3;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.JsonValidator;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.ValidationContext;


/**
 * Represents a function that creates a new instance of validator.
 */
@FunctionalInterface
public interface ValidatorInstance {
  JsonValidator apply(
    final ValidationContext<OAI3> context,
    final JsonNode schemaNode,
    final JsonNode schemaParentNode,
    final SchemaValidator parentSchema);
}
