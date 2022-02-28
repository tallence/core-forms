package com.tallence.formeditor.parser.form;

import com.coremedia.cap.content.Content;
import com.tallence.formeditor.elements.FormElement;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This service can be used in different places (for example in validators, cae or headless server) to create the list
 * of form elements based on the given form editor content object. The service contains a list of
 * {@link FormEditorParser} beans.
 */
@Component
public class FormEditorParserService {

  private final List<FormEditorParser> formEditorParsers;

  public FormEditorParserService(List<FormEditorParser> formEditorParsers) {
    this.formEditorParsers = formEditorParsers;
  }

  public List<FormElement<?>> parseFormElements(Content formEditor) {
    return formEditorParsers.stream()
            .filter(formEditorParser -> formEditorParser.isResponsible(formEditor))
            .findFirst()
            .map(formEditorParser -> formEditorParser.parseFormElements(formEditor))
            .orElseThrow(() -> new IllegalArgumentException("No FormEditorParser found, cannot create form elements.."));
  }
}
