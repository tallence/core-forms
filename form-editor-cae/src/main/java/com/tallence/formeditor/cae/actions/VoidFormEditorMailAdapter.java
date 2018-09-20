package com.tallence.formeditor.cae.actions;

import com.tallence.formeditor.cae.elements.FormElement;
import com.tallence.formeditor.contentbeans.FormEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Component
public class VoidFormEditorMailAdapter implements FormEditorMailAdapter {
  private static final Logger LOGGER = LoggerFactory.getLogger(VoidFormEditorMailAdapter.class);
  @Override
  public boolean sendAdminMail(FormEditor target, String recipient, String formData, List<FormElement> elements) {
    LOGGER.debug("Send Admin Mail, Recipient: " + recipient + " form data: " + formData);
    return true;
  }

  @Override
  public boolean sendUserMail(FormEditor target, String recipient, String formData, List<FormElement> elements, List<MultipartFile> files) {
    LOGGER.debug("Send Admin Mail, Recipient: " + recipient + " form data: " + formData);
    return true;
  }
}
