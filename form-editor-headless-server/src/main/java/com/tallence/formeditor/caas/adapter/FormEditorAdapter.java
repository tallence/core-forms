package com.tallence.formeditor.caas.adapter;

import com.coremedia.cap.content.Content;
import com.coremedia.cap.multisite.SitesService;
import com.tallence.formeditor.FormEditorHelper;
import com.tallence.formeditor.FormElementFactory;
import com.tallence.formeditor.elements.FormElement;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FormEditorAdapter {
  private final Content content;
  private final SitesService sitesService;
  private final FormElementFactory formElementFactory;

  public FormEditorAdapter(Content content, SitesService sitesService, FormElementFactory formElementFactory) {
    this.content = content;
    this.sitesService = sitesService;
    this.formElementFactory = formElementFactory;
  }

  @SuppressWarnings("unused")
  public List<FormElement<?>> formElements() {
    return FormEditorHelper.parseFormElements(content, formElementFactory);
  }

  @SuppressWarnings("unused")
  public List<String> adminEmails() {

    String[] adminMails = Optional.ofNullable(content.getString(FormEditorHelper.ADMIN_MAILS)).orElse("").split(",");
    return Arrays.asList(adminMails);
  }
}
