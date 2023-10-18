package io.programmingnotes.apigenerator.service.oapi.validation.v3;



import io.programmingnotes.apigenerator.data.oapi.v3.OpenApi3;
import io.programmingnotes.apigenerator.data.oapi.v3.Xml;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationContext;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationResults;
import io.programmingnotes.apigenerator.service.oapi.validation.Validator;

import static io.programmingnotes.apigenerator.service.oapi.validation.v3.OAI3Keywords.CRUMB_EXTENSIONS;
import static io.programmingnotes.apigenerator.service.oapi.validation.v3.OAI3Keywords.CRUMB_NAMESPACE;

class XmlValidator extends Validator3Base<OpenApi3, Xml> {
  private static final Validator<OpenApi3, Xml> INSTANCE = new XmlValidator();

  private XmlValidator() {
  }

  public static Validator<OpenApi3, Xml> instance() {
    return INSTANCE;
  }

  @Override
  public void validate(ValidationContext<OpenApi3> context, OpenApi3 api, Xml xml, ValidationResults results) {
    // VALIDATION EXCLUSIONS :
    // name, prefix, attribute, wrapped
    validateUri(xml.getNamespace(), results, false, false, CRUMB_NAMESPACE);
    validateMap(context, api, xml.getExtensions(), results, false, CRUMB_EXTENSIONS, Regexes.EXT_REGEX, null);
  }
}
