package com.tallence.formeditor.caas.adapter;

import com.coremedia.cap.content.Content;
import com.coremedia.cap.content.ContentRepository;
import com.coremedia.cap.struct.Struct;
import com.tallence.formeditor.cae.FormElementFactory;
import com.tallence.formeditor.cae.elements.FormElement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FormEditorAdapter {
  private final Content content;
  private final FormElementFactory formElementFactory;
  private final String propertyName;

  String FORM_ELEMENTS = "formElements";

  public FormEditorAdapter(Content content, FormElementFactory formElementFactory, String propertyName) {
    this.content = content;
    this.formElementFactory = formElementFactory;
    this.propertyName = propertyName;
  }

  @SuppressWarnings("unused")
  public List<FormElement> formElements() {
    Struct formData = content.getStruct(propertyName).getStruct(FORM_ELEMENTS);
    if (formData == null) {
      return Collections.emptyList();
    }
    return formData.getProperties().entrySet().stream()
            .filter(e -> e.getValue() instanceof Struct)
            .map(e -> parseElement((Struct) e.getValue(), e.getKey()))
            .collect(Collectors.toList());
  }

  @SuppressWarnings("unused")
  public List<String> adminEmails() {

    String[] adminMails = Optional.ofNullable(content.getString(propertyName)).orElse("").split(",");
    return Arrays.asList(adminMails);
  }

  private FormElement parseElement(Struct value, String key) {
    return formElementFactory.createFormElement(value, key);
  }
}
