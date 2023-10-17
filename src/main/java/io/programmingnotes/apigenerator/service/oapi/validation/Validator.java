package io.programmingnotes.apigenerator.service.oapi.validation;


import io.programmingnotes.apigenerator.data.oapi.OAI;

public interface Validator<O extends OAI, T> {
  void validate(ValidationContext<O> context, O api, T object, ValidationResults results);
}
