package com.tallence.formeditor.caas.adapter;

import com.coremedia.cap.content.Content;
import com.tallence.formeditor.cae.FormElementFactory;

public class FormEditorAdapterFactory {
  private final FormElementFactory formElementFactory;

  public FormEditorAdapterFactory(FormElementFactory formElementFactory) {
    this.formElementFactory = formElementFactory;
  }

  public FormEditorAdapter to(Content content) {
    return new FormEditorAdapter(content, formElementFactory);
  }
}
