package com.tallence.formeditor.parser.form;

import com.coremedia.cap.content.Content;
import com.tallence.formeditor.elements.FormElement;

import java.util.List;

/**
 * The interface is responsible for parsing the form elements based on a given content object for the form. By default,
 * there is the {@link DefaultFormEditorParser} parser implementing this interface, which creates the form elements
 * based on the information stored in the struct. If the fields for a form are stored in a third party system this interface
 * can be implemented so that the fields do not have to be configured in the studio.
 */
public interface FormEditorParser {

  /**
   * Returns true if the parser is responsible for the given content. For example, a check can be made against the form
   * action stored in the content.
   */
  boolean isResponsible(Content formEditor);

  /**
   * Returns a list of form elements for the given content.
   */
  List<FormElement<?>> parseFormElements(Content formEditor);
}
