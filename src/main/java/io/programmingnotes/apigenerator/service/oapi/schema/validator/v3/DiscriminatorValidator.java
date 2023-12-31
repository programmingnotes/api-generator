package io.programmingnotes.apigenerator.service.oapi.schema.validator.v3;

import com.fasterxml.jackson.databind.JsonNode;
import io.programmingnotes.apigenerator.data.oapi.reference.Reference;
import io.programmingnotes.apigenerator.data.oapi.reference.ReferenceRegistry;
import io.programmingnotes.apigenerator.data.oapi.v3.OAI3;
import io.programmingnotes.apigenerator.data.oapi.v3.OAI3SchemaKeywords;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.BaseJsonValidator;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.ValidationContext;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.ValidationData;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResult;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResults;


import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

import static io.programmingnotes.apigenerator.data.oapi.reference.Reference.ABS_REF_FIELD;
import static io.programmingnotes.apigenerator.data.oapi.v3.OAI3SchemaKeywords.*;
import static io.programmingnotes.apigenerator.service.oapi.validation.ValidationSeverity.ERROR;

/**
 * discriminator keyword validator.
 * <p/>
 * <a href="https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.2.md#discriminatorObject" />
 */
abstract class DiscriminatorValidator extends BaseJsonValidator<OAI3> {
  private static final ValidationResult INVALID_SCHEMA_ERR = new ValidationResult(ERROR, 1003, "Schema selection can't be made for discriminator '%s' with value '%s'.");
  private static final ValidationResult INVALID_PROPERTY_ERR = new ValidationResult(ERROR, 1004, "Property name in schema is not set.");
  private static final ValidationResult INVALID_PROPERTY_CONTENT_ERR = new ValidationResult(ERROR, 1005, "Property name in content '%s' is not set.");

  private static final ValidationResults.CrumbInfo CRUMB_INFO = new ValidationResults.CrumbInfo(DISCRIMINATOR, true);

  private static final String SCHEMAS_PATH = "#/components/schemas/";

  final List<SchemaValidator> validators = new ArrayList<>();
  private final String arrayType;
  private JsonNode discriminatorNode;
  private String discriminatorPropertyName;
  private JsonNode discriminatorMapping;
  private final ValidationResults.CrumbInfo crumbInfo;

  DiscriminatorValidator(final ValidationContext<OAI3> context,
                         final JsonNode schemaNode,
                         final JsonNode schemaParentNode,
                         final SchemaValidator parentSchema,
                         final String arrayType) {

    super(context, schemaNode, schemaParentNode, parentSchema);

    this.arrayType = arrayType;
    crumbInfo = new ValidationResults.CrumbInfo(arrayType, true);

    // Setup discriminator behaviour for anyOf, oneOf or allOf
    setupDiscriminator(context, schemaNode, schemaParentNode, parentSchema);
  }

  @Override
  public boolean validate(final JsonNode valueNode, final ValidationData<?> validation) {
    if (discriminatorNode != null) {
      String discriminatorValue = getDiscriminatorValue(valueNode, validation);
      if (discriminatorValue != null) {
        if (ALLOF.equals(arrayType)) {
          validateAllOf(valueNode, discriminatorValue, validation);
        } else {
          validateOneAnyOf(valueNode, discriminatorValue, validation);
        }
      }
    } else {
      validateWithoutDiscriminator(valueNode, validation);
    }

    return false;
  }

  private void validateAllOf(final JsonNode valueNode, final String discriminatorValue, final ValidationData<?> validation) {
    if (!checkAllOfValidator(discriminatorValue)) {
      validation.add(CRUMB_INFO, INVALID_SCHEMA_ERR, discriminatorPropertyName, discriminatorValue);
      return;
    }

    validate(() -> {
      for (SchemaValidator validator : validators) {
        validator.validateWithContext(valueNode, validation);
      }
    });
  }

  private void validateOneAnyOf(final JsonNode valueNode, final String discriminatorValue, final ValidationData<?> validation) {
    SchemaValidator validator = getOneAnyOfValidator(discriminatorValue);
    if (validator == null) {
      validation.add(CRUMB_INFO, INVALID_SCHEMA_ERR, discriminatorPropertyName, discriminatorValue);
      return;
    }

    validate(() -> validator.validateWithContext(valueNode, validation));
  }

  /**
   * Validate array keyword with default behaviour.
   *
   * @param valueNode  The current value node.
   * @param validation The validation results.
   */
  abstract void validateWithoutDiscriminator(final JsonNode valueNode, final ValidationData<?> validation);

  private void setupDiscriminator(final ValidationContext<OAI3> context,
                                  final JsonNode schemaNode,
                                  final JsonNode schemaParentNode,
                                  final SchemaValidator parentSchema) {

    if (ALLOF.equals(arrayType)) {
      setupAllOfDiscriminatorSchemas(context, schemaNode, schemaParentNode, parentSchema);
    } else {
      setupAnyOneOfDiscriminatorSchemas(context, schemaNode, schemaParentNode, parentSchema);
    }

    if (discriminatorNode != null) {
      JsonNode propertyNameNode = discriminatorNode.get(PROPERTYNAME);
      if (propertyNameNode == null) {
        // Property name in schema is not set. This will result in error on validate call.
        return;
      }

      discriminatorPropertyName = propertyNameNode.textValue();
      discriminatorMapping = discriminatorNode.get(MAPPING);
    }
  }

  private void setupAllOfDiscriminatorSchemas(final ValidationContext<OAI3> context,
                                              final JsonNode schemaNode,
                                              final JsonNode schemaParentNode,
                                              final SchemaValidator parentSchema) {

    JsonNode allOfNode = getParentSchemaNode().get(ALLOF);
    ReferenceRegistry refRegistry = context.getContext().getReferenceRegistry();

    for (JsonNode allOfNodeItem : allOfNode) {
      List<JsonNode> refNodes = getReferences(allOfNodeItem);

      for (JsonNode refNode : refNodes) {
        Reference reference = refRegistry.getRef(refNode.textValue());
        discriminatorNode = reference.getContent().get(DISCRIMINATOR);
        if (discriminatorNode != null) {
          setupAllOfDiscriminatorSchemas(schemaNode, refNode, reference, schemaParentNode, parentSchema);
          return;
        }
      }
    }

    // Add default schemas
    for (JsonNode node : schemaNode) {
      validators.add(new SchemaValidator(context, crumbInfo, node, schemaParentNode, parentSchema));
    }
  }

  private void setupAnyOneOfDiscriminatorSchemas(final ValidationContext<OAI3> context,
                                                 final JsonNode schemaNode,
                                                 final JsonNode schemaParentNode,
                                                 final SchemaValidator parentSchema) {

    discriminatorNode = getParentSchemaNode().get(DISCRIMINATOR);

    for (JsonNode node : schemaNode) {
      validators.add(new SchemaValidator(context, crumbInfo, node, schemaParentNode, parentSchema));
    }
  }

  private void setupAllOfDiscriminatorSchemas(final JsonNode schemaNode,
                                              final JsonNode refNode,
                                              final Reference reference,
                                              final JsonNode schemaParentNode,
                                              final SchemaValidator parentSchema) {

    for (JsonNode node : schemaNode) {
      JsonNode refValueNode = getReference(node);

      if (refNode.equals(refValueNode)) { // Add the parent schema
        ValidationResults.CrumbInfo refCrumbInfo = new ValidationResults.CrumbInfo(reference.getRef(), true);
        validators.add(new SchemaValidator(context, refCrumbInfo, reference.getContent(), schemaParentNode, parentSchema));
      } else { // Add the other items
        validators.add(new SchemaValidator(context, crumbInfo, node, schemaParentNode, parentSchema));
      }
    }
  }

  private List<JsonNode> getReferences(JsonNode allOfNodeItem) {
    // Prefer absolute reference value
    List<JsonNode> refNodes = allOfNodeItem.findValues(ABS_REF_FIELD);
    if (refNodes.isEmpty()) {
      refNodes = allOfNodeItem.findValues(OAI3SchemaKeywords.$REF);
    }

    return refNodes;
  }

  private JsonNode getReference(JsonNode node) {
    // Prefer absolute reference value
    JsonNode refNode = node.get(ABS_REF_FIELD);
    if (refNode == null) {
      refNode = node.get(OAI3SchemaKeywords.$REF);
    }

    return refNode;
  }

  private String getDiscriminatorValue(final JsonNode valueNode, final ValidationData<?> validation) {
    // check discriminator definition
    if (discriminatorPropertyName == null) {
      validation.add(CRUMB_INFO, INVALID_PROPERTY_ERR);
      return null;
    }
    // check discriminator in content
    JsonNode discriminatorPropertyNameNode = valueNode.get(discriminatorPropertyName);
    if (discriminatorPropertyNameNode == null) {
      validation.add(CRUMB_INFO, INVALID_PROPERTY_CONTENT_ERR, discriminatorPropertyName);
      return null;
    }

    return discriminatorPropertyNameNode.textValue();
  }

  private boolean checkAllOfValidator(final String discriminatorValue) {
    String ref = null;

    // Explicit case with mapping
    if (discriminatorMapping != null) {
      JsonNode mappingNode = discriminatorMapping.get(discriminatorValue);
      if (mappingNode != null) {
        ref = mappingNode.textValue();
      }
    }

    // Implicit case, the value must match exactly one schema in "#/components/schemas/"
    if (ref == null) {
      ref = SCHEMAS_PATH + discriminatorValue;
    }

    // Check if Schema Object exists
    return context.getContext().getReferenceRegistry().getRef(ref) != null;
  }

  private SchemaValidator getOneAnyOfValidator(final String discriminatorValue) {
    // Explicit case with mapping
    if (discriminatorMapping != null) {
      JsonNode mappingNode = discriminatorMapping.get(discriminatorValue);
      if (mappingNode != null) {
        String ref = mappingNode.textValue();
        SchemaValidator validator = getOneAnyOfValidator(ref, String::equals);
        if (validator != null) {
          return validator;
        }
      }
    }

    // Implicit case, the value must match exactly one of the schemas name regardless path
    return getOneAnyOfValidator(discriminatorValue, String::endsWith);
  }

  private SchemaValidator getOneAnyOfValidator(final String value,
                                               final BiPredicate<String, String> checker) {

    for (SchemaValidator validator : validators) {
      JsonNode refNode = validator.getSchemaNode().get($REF);
      if (refNode != null && checker.test(refNode.textValue(), value)) {
        return validator;
      }
    }

    return null;
  }
}
