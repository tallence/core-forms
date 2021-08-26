package com.tallence.formeditor.caas.adapter;

import com.coremedia.cap.content.Content;
import com.coremedia.cap.multisite.SitesService;
import com.tallence.formeditor.FormElementFactory;

import java.util.Locale;

public class FormEditorAdapterFactory {
  private final FormElementFactory formElementFactory;
  private final SitesService sitesService;
  private final ThreadLocal<Locale> localeThreadLocal;

  public FormEditorAdapterFactory(FormElementFactory formElementFactory, SitesService sitesService, ThreadLocal<Locale> localeThreadLocal) {
    this.formElementFactory = formElementFactory;
    this.sitesService = sitesService;
    this.localeThreadLocal = localeThreadLocal;
  }

  public FormEditorAdapter to(Content content) {
    return new FormEditorAdapter(content, sitesService, formElementFactory, localeThreadLocal);
  }
}
