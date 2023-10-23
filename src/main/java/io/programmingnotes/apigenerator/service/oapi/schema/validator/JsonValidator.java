package io.programmingnotes.apigenerator.service.oapi.schema.validator;

import com.fasterxml.jackson.databind.JsonNode;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationException;

/**
 * Representation of a validator.
 */
public interface JsonValidator {
  /**
   * Validate the given value from the validation setup.
   *
   * @param valueNode The given value to check.
   * @param validation   The result stack to append any additional info from the validation.
   * @return {@code true} if chain should continue for the current keyword, {@code false} otherwise.
   */
  boolean validate(final JsonNode valueNode, final ValidationData<?> validation);

  /**
   * Validate the given value from the validation setup.
   *
   * @param valueNode The given value to check.
   * @throws ValidationException The result stack info from the validation in case of error.
   */
  void validate(final JsonNode valueNode) throws ValidationException;
}
