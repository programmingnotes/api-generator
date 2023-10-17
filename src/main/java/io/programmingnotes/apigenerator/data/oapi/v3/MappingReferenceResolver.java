package io.programmingnotes.apigenerator.data.oapi.v3;

import com.fasterxml.jackson.databind.JsonNode;
import io.programmingnotes.apigenerator.data.oapi.AuthOption;
import io.programmingnotes.apigenerator.data.oapi.reference.AbstractReferenceResolver;
import io.programmingnotes.apigenerator.data.oapi.reference.ReferenceRegistry;


import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static io.programmingnotes.apigenerator.data.oapi.v3.OAI3SchemaKeywords.$REF;
import static io.programmingnotes.apigenerator.data.oapi.v3.OAI3SchemaKeywords.MAPPING;


/**
 * The JSON reference resolver for discriminator mapping.
 */
class MappingReferenceResolver extends AbstractReferenceResolver {
  MappingReferenceResolver(URL baseUrl, List<AuthOption> authOptions, JsonNode apiNode, ReferenceRegistry referenceRegistry) {
    super(baseUrl, authOptions, apiNode, $REF, referenceRegistry);
  }

  @Override
  protected Collection<JsonNode> getReferencePaths(JsonNode document) {
    Collection<JsonNode> referenceNodes = document.findValues(MAPPING);

    Collection<JsonNode> referencePaths = new HashSet<>();

    for (JsonNode refNode : referenceNodes) {
      for (JsonNode mappingNode : refNode) {
        referencePaths.add(mappingNode);
      }
    }

    return referencePaths;
  }
}
