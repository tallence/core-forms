package com.tallence.formeditor.parser.form;

import com.coremedia.cap.common.CapStruct;
import com.coremedia.cap.content.Content;
import com.coremedia.cap.struct.Struct;
import com.tallence.formeditor.FormEditorHelper;
import com.tallence.formeditor.FormElementFactory;
import com.tallence.formeditor.elements.FormElement;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The default {@link FormEditorParser} is used to create the form elements based on the configuration stored in the
 * struct. The order is set to 1000, so that for an own implementation the check for the custom parser is checked first,
 * because this parser is responsible for all forms per default.
 */
@Order(1000)
@Component
public class DefaultFormEditorParser implements FormEditorParser {

  private final FormElementFactory formElementFactory;

  public DefaultFormEditorParser(FormElementFactory formElementFactory) {
    this.formElementFactory = formElementFactory;
  }

  @Override
  public boolean isResponsible(Content formEditor) {
    return true;
  }

  @Override
  public List<FormElement<?>> parseFormElements(Content form) {
    var formData = FormEditorHelper.getFormElements(form)
            .map(CapStruct::getProperties)
            .orElse(Collections.emptyMap());
    return formData.entrySet().stream()
            .filter(e -> e.getValue() instanceof Struct)
            .map(e -> parseElement((Struct) e.getValue(), e.getKey(), formElementFactory, form))
            .collect(Collectors.toList());
  }

  private static FormElement<?> parseElement(Struct value, String key, FormElementFactory formElementFactory, Content formEditor) {
    return formElementFactory.createFormElement(value, key, formEditor);
  }
}
