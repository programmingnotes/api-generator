package io.programmingnotes.apigenerator.data.oapi;

import com.fasterxml.jackson.databind.JsonNode;
import io.programmingnotes.apigenerator.data.oapi.reference.ReferenceRegistry;

import java.net.URL;

public interface OAIContext {
  /**
   * Get the reference registry.
   *
   * @return The reference registry.
   */
  ReferenceRegistry getReferenceRegistry();

  /**
   * The base URL of the context.
   * @return The base URL of the context.
   */
  URL getBaseUrl();

  /**
   * Get the base document of API description.
   */
  JsonNode getBaseDocument();
}
