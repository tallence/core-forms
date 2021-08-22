package com.tallence.formeditor.caas.adapter;

import com.coremedia.cap.content.Content;
import com.coremedia.cap.multisite.SitesService;
import com.tallence.formeditor.FormElementFactory;

public class FormEditorAdapterFactory {
  private final FormElementFactory formElementFactory;
  private final SitesService sitesService;

  public FormEditorAdapterFactory(FormElementFactory formElementFactory, SitesService sitesService) {
    this.formElementFactory = formElementFactory;
    this.sitesService = sitesService;
  }

  public FormEditorAdapter to(Content content) {
    return new FormEditorAdapter(content, sitesService, formElementFactory);
  }
}
