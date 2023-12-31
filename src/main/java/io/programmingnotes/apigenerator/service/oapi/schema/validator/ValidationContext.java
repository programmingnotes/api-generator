package io.programmingnotes.apigenerator.service.oapi.schema.validator;


import io.programmingnotes.apigenerator.data.oapi.OAI;
import io.programmingnotes.apigenerator.data.oapi.OAIContext;
import io.programmingnotes.apigenerator.service.oapi.schema.validator.v3.ValidatorInstance;
import io.programmingnotes.apigenerator.service.oapi.util.MultiStringMap;


import java.util.HashMap;
import java.util.Map;

/**
 * Validation context and option bag.
 *
 * @param <O> The Open API version type.
 */
@SuppressWarnings("UnusedReturnValue")
public class ValidationContext<O extends OAI> {
  private final OAIContext context;
  private final Map<String, JsonValidator> visitedRefs = new HashMap<>();
  private final Map<Byte, Boolean> defaultOptions = new HashMap<>();
  private final MultiStringMap<ValidatorInstance> additionalValidators = new MultiStringMap<>(true, true);
  private boolean isFastFail;

  public ValidationContext(OAIContext context) {
    this.context = context;
  }

  public OAIContext getContext() {
    return context;
  }

  /**
   * Get the fast fail behaviour status.
   *
   * @return The fast fail behaviour status.
   */
  public boolean isFastFail() {
    return isFastFail;
  }

  /**
   * Set the fast fail behaviour.
   *
   * @param fastFail {@code true} for fast failing.
   */
  public ValidationContext<O> setFastFail(boolean fastFail) {
    isFastFail = fastFail;
    return this;
  }

  /**
   * Add a reference to avoid looping.
   * This is internally used, you should not call this directly.
   *
   * @param ref       The reference expression.
   * @param validator The associated validator.
   */
  public ValidationContext<O> addReference(String ref, JsonValidator validator) {
    visitedRefs.put(ref, validator);
    return this;
  }

  /**
   * Get a visited reference validator in any.
   * This is internally used, you should not call this directly.
   *
   * @param ref The reference expression.
   * @return The associated validator.
   */
  public JsonValidator getReference(String ref) {
    return visitedRefs.get(ref);
  }

  public ValidationContext<O> setOption(byte option, boolean value) {
    defaultOptions.put(option, value);
    return this;
  }

  /**
   * Get the value from the given option name.
   *
   * @param option The given option.
   * @return The corresponding value, {@code false} if the option is not set.
   */
  public boolean getOption(byte option) {
    return Boolean.TRUE.equals(defaultOptions.get(option));
  }

  /**
   * Get the additional validators associated to the context.
   */
  public MultiStringMap<ValidatorInstance> getValidators() {
    return additionalValidators;
  }

  /**
   * Add an additional validator as an override or a custom one for the given keyword.
   * You can setup multiple validators for the same keyword (order matters).
   *
   * @param keyword                The keyword to match.
   * @param validatorInstantiation The instantiation to call when a validation should occur.
   * @return this.
   */
  public ValidationContext<O> addValidator(String keyword, ValidatorInstance validatorInstantiation) {
    additionalValidators.put(keyword, validatorInstantiation);
    return this;
  }
}
