package io.programmingnotes.apigenerator.data.oapi.reference;

import com.fasterxml.jackson.databind.JsonNode;
import io.programmingnotes.apigenerator.data.oapi.AuthOption;

import java.net.URL;
import java.util.Collection;
import java.util.List;

/**
 * The default JSON reference resolver.
 */
public class ReferenceResolver extends AbstractReferenceResolver {
  public ReferenceResolver(URL baseUrl, List<AuthOption> authOptions, JsonNode apiNode, String refKeyword, ReferenceRegistry referenceRegistry) {
    super(baseUrl, authOptions, apiNode, refKeyword, referenceRegistry);
  }

  @Override
  protected Collection<JsonNode> getReferencePaths(JsonNode document) {
    return document.findValues(refKeyword);
  }
}
