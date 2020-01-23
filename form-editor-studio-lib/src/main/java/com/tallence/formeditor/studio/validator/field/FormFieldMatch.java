package com.tallence.formeditor.studio.validator.field;

import java.util.ArrayList;
import java.util.List;

/**
 * A CounterPart to {@link com.coremedia.rest.validation.Issues}.
 * It is not used to identify errors, but to identify fields, which fulfil specific requirements. It can be treated as
 * a "positive validator" and might be used to make sure, required form fields are present.
 *
 * A simple List of Strings would have done the job too. But this implementation is more similar to
 * {@link com.coremedia.rest.validation.Issues} and might be enriched by further programmers.
 */
public class FormFieldMatch {

  private List<String> fieldMatches = new ArrayList<>();

  public void registerFieldMatch(String fieldId, String matchedFieldIdentifier) {
    fieldMatches.add(matchedFieldIdentifier);
  }

  /**
   * Checks if the given identifier has already been registered as "matched".
   */
  public boolean containsId(String fieldIdentifier) {
    return fieldMatches.contains(fieldIdentifier);
  }
}
