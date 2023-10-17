package io.programmingnotes.apigenerator.data.oapi.v3;

import com.fasterxml.jackson.databind.JsonNode;
import io.programmingnotes.apigenerator.data.oapi.AuthOption;
import io.programmingnotes.apigenerator.data.oapi.OAIContext;
import io.programmingnotes.apigenerator.data.oapi.reference.ReferenceRegistry;
import io.programmingnotes.apigenerator.data.oapi.reference.ReferenceResolver;
import io.programmingnotes.apigenerator.exception.ResolutionException;


import java.net.URL;
import java.util.List;

import static io.programmingnotes.apigenerator.data.oapi.v3.OAI3SchemaKeywords.$REF;


/**
 * Open API v3 context.
 * <p/>
 * This class handles references and base URL.
 */
public class OAI3Context implements OAIContext {
  private static final String OPERATION_REF = "operationRef";

  private final ReferenceRegistry referenceRegistry;
  private final URL baseUrl;
  private final List<AuthOption> authOptions;
  private final JsonNode baseDocument;

  /**
   * Creates a context from the given url.
   *
   * @param baseUrl The given url.
   * @throws ResolutionException In case of missing or wrong reference during discovery process.
   */
  public OAI3Context(URL baseUrl) throws ResolutionException {
    this(baseUrl, null, null);
  }

  /**
   * Creates a context from the given url.
   *
   * @param baseUrl     The given url.
   * @param authOptions The authentication values.
   * @throws ResolutionException In case of missing or wrong reference during discovery process.
   */
  public OAI3Context(URL baseUrl, List<AuthOption> authOptions) throws ResolutionException {
    this(baseUrl, authOptions, null);
  }

  /**
   * Creates a context from the given url.
   *
   * @param baseUrl      The given url.
   * @param baseDocument The tree node representing the Open API schema.
   * @throws ResolutionException In case of missing or wrong reference during discovery process.
   */
  public OAI3Context(URL baseUrl, JsonNode baseDocument) throws ResolutionException {
    this(baseUrl, null, baseDocument);
  }

  /**
   * Creates a context from the given url.
   *
   * @param baseUrl      The given url.
   * @param authOptions  The authentication values.
   * @param baseDocument The tree node representing the Open API schema.
   * @throws ResolutionException In case of missing or wrong reference during discovery process.
   */
  public OAI3Context(URL baseUrl, List<AuthOption> authOptions, JsonNode baseDocument) throws ResolutionException {
    this.baseUrl = baseUrl;
    referenceRegistry = new ReferenceRegistry(baseUrl);
    this.authOptions = authOptions;
    this.baseDocument = resolveReferences(baseDocument);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ReferenceRegistry getReferenceRegistry() {
    return referenceRegistry;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JsonNode getBaseDocument() {
    return baseDocument;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public URL getBaseUrl() {
    return baseUrl;
  }

  private JsonNode resolveReferences(JsonNode baseDocument) throws ResolutionException {
    // Standard JSON references
    ReferenceResolver resolver = new ReferenceResolver(baseUrl, authOptions, baseDocument, $REF, referenceRegistry);
    resolver.resolve();

    // Mapping JSON references
    ReferenceRegistry mappingRefsRegistry = new ReferenceRegistry(baseUrl);
    MappingReferenceResolver mappingResolver = new MappingReferenceResolver(baseUrl, authOptions, baseDocument, mappingRefsRegistry);
    mappingResolver.resolve();
    referenceRegistry.mergeRefs(mappingRefsRegistry);

    // Links JSON references
    ReferenceRegistry operationRefsRegistry = new ReferenceRegistry(baseUrl);
    ReferenceResolver operationResolver = new ReferenceResolver(baseUrl, authOptions, baseDocument, OPERATION_REF, operationRefsRegistry);
    operationResolver.resolve();
    referenceRegistry.mergeRefs(operationRefsRegistry);

    return resolver.getBaseDocument();
  }
}
