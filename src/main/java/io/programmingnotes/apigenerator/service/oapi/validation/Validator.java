package io.programmingnotes.apigenerator.service.oapi.validator;

import org.openapi4j.core.model.OAI;
import org.openapi4j.core.validation.ValidationResults;

public interface Validator<O extends OAI, T> {
  void validate(ValidationContext<O> context, O api, T object, ValidationResults results);
}
