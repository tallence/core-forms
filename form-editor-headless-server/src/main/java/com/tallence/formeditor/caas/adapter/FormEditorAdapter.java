package com.tallence.formeditor.caas.adapter;

import com.coremedia.cap.content.Content;
import com.coremedia.cap.multisite.SitesService;
import com.coremedia.cap.struct.Struct;
import com.tallence.formeditor.cae.FormEditorHelper;
import com.tallence.formeditor.cae.FormElementFactory;
import com.tallence.formeditor.cae.elements.FormElement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

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
  public List<FormElement> formElements() {
    Struct formData = content.getStruct(FormEditorHelper.FORM_DATA).getStruct(FormEditorHelper.FORM_ELEMENTS);
    if (formData == null) {
      return Collections.emptyList();
    }
    return formData.getProperties().entrySet().stream()
            .filter(e -> e.getValue() instanceof Struct)
            .map(e -> parseElement((Struct) e.getValue(), e.getKey(), sitesService.getContentSiteAspect(content).getLocale()))
            .collect(Collectors.toList());
  }

  @SuppressWarnings("unused")
  public List<String> adminEmails() {

    String[] adminMails = Optional.ofNullable(content.getString(FormEditorHelper.ADMIN_MAILS)).orElse("").split(",");
    return Arrays.asList(adminMails);
  }

  private FormElement parseElement(Struct value, String key, Locale locale) {
    return formElementFactory.createFormElement(value, key, locale);
  }
}
