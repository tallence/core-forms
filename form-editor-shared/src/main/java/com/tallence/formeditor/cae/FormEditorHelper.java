package com.tallence.formeditor.cae;

import com.coremedia.cap.common.CapStructHelper;
import com.coremedia.cap.content.Content;
import com.coremedia.cap.struct.Struct;

import java.util.Optional;

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
    if (!content.getType().isSubtypeOf("FormEditor")) {
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
}
