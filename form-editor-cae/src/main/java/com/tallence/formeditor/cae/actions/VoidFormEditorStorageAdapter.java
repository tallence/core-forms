package com.tallence.formeditor.cae.actions;

import com.tallence.formeditor.cae.elements.FormElement;
import com.tallence.formeditor.contentbeans.FormEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class VoidFormEditorStorageAdapter implements FormEditorStorageAdapter {
  private static final Logger LOGGER = LoggerFactory.getLogger(VoidFormEditorStorageAdapter.class);

  @Override
  public boolean persistFormData(FormEditor target, String formData, List<FormElement> elements, List<MultipartFile> files) {
    LOGGER.debug(formData);
    return true;
  }
}
