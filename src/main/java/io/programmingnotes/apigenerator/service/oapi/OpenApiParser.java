package io.programmingnotes.apigenerator.service.oapi;



import com.fasterxml.jackson.databind.JsonNode;
import io.programmingnotes.apigenerator.data.oapi.AuthOption;
import io.programmingnotes.apigenerator.data.oapi.OAI;
import io.programmingnotes.apigenerator.exception.ResolutionException;
import io.programmingnotes.apigenerator.service.oapi.validation.ValidationException;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Base Open API parser
 *
 * @param <O> Open API model
 */
public abstract class OpenApiParser<O extends OAI> {
  private static final String INVALID_FILE = "File must be specified";
  private static final String INVALID_URL = "Unable to read from url";

  /**
   * Parse the Open API specification from the given file.
   *
   * @param specFile The given file.
   * @param validate {@code true} for validation, {@code false} otherwise.
   * @return The Open API model
   * @throws ResolutionException In case of wrong path, JSON reference issue.
   * @throws ValidationException In case of validation error.
   */
  public O parse(File specFile, boolean validate) throws ResolutionException, ValidationException {
    if (specFile == null) {
      throw new ResolutionException(INVALID_FILE);
    }

    try {
      return parse(specFile.toURI().toURL(), validate);
    } catch (MalformedURLException e) {
      throw new ResolutionException(INVALID_URL, e);
    }
  }

  /**
   * Parse the Open API specification from the given URL.
   *
   * @param url      The given URL.
   * @param validate {@code true} for validation, {@code false} otherwise.
   * @return The Open API model
   * @throws ResolutionException In case of wrong path, JSON reference issue.
   * @throws ValidationException In case of validation error.
   */
  public O parse(URL url, boolean validate) throws ResolutionException, ValidationException {
    return parse(url, null, null, validate);
  }

  /**
   * Parse the Open API specification from the given URL with authentication values.
   *
   * @param url         The given URL.
   * @param authOptions The given authentication values for all the chain to resolve.
   * @param validate    {@code true} for validation, {@code false} otherwise.
   * @return The Open API model
   * @throws ResolutionException In case of wrong path, JSON reference issue.
   * @throws ValidationException In case of validation error.
   */
  public abstract O parse(URL url, List<AuthOption> authOptions, JsonNode basedocument, boolean validate) throws ResolutionException, ValidationException;
}
