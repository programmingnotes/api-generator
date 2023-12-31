package io.programmingnotes.apigenerator.service.oapi.validation;



import io.programmingnotes.apigenerator.data.oapi.OAI;

import java.util.HashSet;
import java.util.Set;

public class ValidationContext<O extends OAI> {
  private final Set<Integer> visitedElements = new HashSet<>();

  public <V> void validate(O api, V value, final Validator<O, V> validator, ValidationResults results) {
    if (!visitedElements.add(value.hashCode())) {
      return;
    }

    validator.validate(this, api, value, results);
  }
}
