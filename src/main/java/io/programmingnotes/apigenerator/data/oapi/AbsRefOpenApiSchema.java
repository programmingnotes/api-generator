package io.programmingnotes.apigenerator.data.oapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.programmingnotes.apigenerator.data.oapi.reference.Reference;


import java.net.URL;

import static io.programmingnotes.apigenerator.data.oapi.reference.Reference.ABS_REF_FIELD;
import static io.programmingnotes.apigenerator.data.oapi.v3.OAI3SchemaKeywords.$REF;


/**
 * Base class for Open API schema which can be represented as reference.
 */
public abstract class AbsRefOpenApiSchema<M extends OpenApiSchema<M>> extends AbsOpenApiSchema<M> {
  @JsonProperty($REF)
  private String ref;
  @JsonProperty(value = ABS_REF_FIELD)
  @JsonView(Views.Internal.class)
  private String canonicalRef;

  // $ref
  public String getRef() {
    return ref;
  }

  public boolean isRef() {
    return ref != null;
  }

  protected void setRef(String ref) {
    this.ref = ref;
  }

  protected void setCanonicalRef(String canonicalRef) {
    this.canonicalRef = canonicalRef;
  }

  public String getCanonicalRef() {
    return canonicalRef;
  }

  public Reference getReference(OAIContext context) {
    return context.getReferenceRegistry().getRef(canonicalRef != null ? canonicalRef : ref);
  }

  public Reference setReference(OAIContext context, URL url, String ref) {
    Reference reference = context.getReferenceRegistry().addRef(url, ref);
    setRef(reference.getRef());
    setCanonicalRef(reference.getCanonicalRef());

    return reference;
  }
}
