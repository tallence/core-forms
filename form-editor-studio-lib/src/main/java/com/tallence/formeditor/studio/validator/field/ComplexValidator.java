package com.tallence.formeditor.studio.validator.field;

import com.coremedia.cap.content.Content;
import com.coremedia.rest.validation.Issues;
import com.tallence.formeditor.elements.FormElement;

import java.util.List;

/**
 * Defines a more complex interface for form editor validators compared to {@link FieldValidator}.
 * May take other formElements and the form documents into account.
 */
public interface ComplexValidator {

  /**
   * Validates a form and it's fields.
   *
   * @param formElements the parsed {@link FormElement}s
   * @param action    the current action of the form (some validators need this).
   * @param issues    the Issues object for the current validation request.
   * @param document  the document, containing the struct property with all forms.
   */
  void validateFieldIfResponsible(List<FormElement<?>> formElements, String action, Issues issues, Content document);

}
