package io.programmingnotes.apigenerator.service.oapi.schema.validator.v3;

import com.fasterxml.jackson.databind.JsonNode;
import io.programmingnotes.apigenerator.data.oapi.v3.OAI3;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.BaseJsonValidator;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.ValidationContext;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.ValidationData;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResults;

import static io.programmingnotes.apigenerator.data.oapi.v3.OAI3SchemaKeywords.ITEMS;


/**
 * items keyword validator.
 * <p/>
 * <a href="https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.2.md#schemaObject" />
 * <p/>
 * <a href="https://tools.ietf.org/html/draft-wright-json-schema-validation-00#page-7" />
 */
class ItemsValidator extends BaseJsonValidator<OAI3> {
  private static final ValidationResults.CrumbInfo CRUMB_INFO = new ValidationResults.CrumbInfo(ITEMS, true);
  private final SchemaValidator schema;

  ItemsValidator(final ValidationContext<OAI3> context, final JsonNode schemaNode, final JsonNode schemaParentNode, final SchemaValidator parentSchema) {
    super(context, schemaNode, schemaParentNode, parentSchema);

    schema = new SchemaValidator(context, CRUMB_INFO, schemaNode, schemaParentNode, parentSchema);
  }

  @Override
  public boolean validate(final JsonNode valueNode, final ValidationData<?> validation) {
    if (!valueNode.isArray()) {
      return false;
    }

    for (int idx = 0; idx < valueNode.size(); ++idx) {
      JsonNode itemNode = valueNode.get(idx);

      validation.results().withCrumb(
        new ValidationResults.CrumbInfo(Integer.toString(idx), false),
        () -> schema.validate(itemNode, validation));

      if (context.isFastFail() && !validation.isValid()) {
        break;
      }
    }

    return false;
  }
}
