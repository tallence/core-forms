package com.tallence.formeditor.caas.adapter;

import com.coremedia.cap.content.Content;
import com.coremedia.cap.content.ContentRepository;
import com.tallence.formeditor.cae.FormElementFactory;

public class FormEditorAdapterFactory {
  private final FormElementFactory formElementFactory;

  public FormEditorAdapterFactory(FormElementFactory formElementFactory) {
    this.formElementFactory = formElementFactory;
  }

  public FormEditorAdapter to(Content content, String propertyName) {
    return new FormEditorAdapter(content, formElementFactory, propertyName);
  }
}
