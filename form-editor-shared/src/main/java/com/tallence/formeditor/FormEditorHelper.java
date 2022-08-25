package com.tallence.formeditor;

import com.coremedia.cap.common.CapStruct;
import com.coremedia.cap.common.CapStructHelper;
import com.coremedia.cap.content.Content;
import com.coremedia.cap.struct.Struct;
import com.tallence.formeditor.contentbeans.FormEditor;
import com.tallence.formeditor.elements.FormElement;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FormEditorHelper {

  /**
   * The keys are placed here, since they are used by studio, cae and headless server.
   */

  public static String FORM_ELEMENTS = "formElements";

  public static String FORM_DATA = "formData";

  public static String FORM_ACTION = "formAction";

  public static String ADMIN_MAILS = "adminMails";

  public static String FORM_SPAM_PROTECTION = "spamProtectionEnabled";

  public static String MAIL_ACTION = "mailAction";

  public static Optional<Struct> getFormElements(Content content) {
    if (!content.getType().isSubtypeOf(FormEditor.NAME)) {
      return Optional.empty();
    }
    Struct formData = content.getStruct(FORM_DATA);
    if (formData == null) {
      return Optional.empty();
    }
    Struct formElements = CapStructHelper.getStruct(formData, FORM_ELEMENTS);
    if (formElements == null) {
      return Optional.empty();
    }
    return Optional.of(formElements);
  }

  /**
   * Delegating to {@link #parseFormElements(Map, FormElementFactory)}
   * @param form the document containing all form elements data
   * @param formElementFactory the factory to create {@link FormElement}
   * @return a list of parsed {@link FormElement}s
   */
  public static List<FormElement<?>> parseFormElements(Content form, FormElementFactory formElementFactory) {

    var formData = getFormElements(form)
            .map(CapStruct::getProperties)
            .orElse(Collections.emptyMap());
    return parseFormElements(formData, formElementFactory);
  }

  /**
   * Resolves the {@link #FORM_ELEMENTS} Struct and transforms the element data into {@link FormElement}s.
   * @param formData the document containing all form elements data
   * @param formElementFactory the factory to create {@link FormElement}
   * @return a list of parsed {@link FormElement}s
   */
  public static List<FormElement<?>> parseFormElements(Map<String, Object> formData, FormElementFactory formElementFactory) {

    return formData.entrySet().stream()
            .filter(e -> e.getValue() instanceof Struct)
            .map(e -> parseElement((Struct) e.getValue(), e.getKey(), formElementFactory))
            .collect(Collectors.toList());
  }

  private static FormElement<?> parseElement(Struct value, String key, FormElementFactory formElementFactory) {
    return formElementFactory.createFormElement(value, key);
  }
}
