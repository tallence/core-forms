/*
 * Copyright 2018 Tallence AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tallence.formeditor.cae.validator;

import com.tallence.formeditor.cae.elements.FileUpload;
import com.tallence.formeditor.cae.validator.annotation.ValidationProperty;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Validator for elements of type {@link FileUpload}
 */
public class FileUploadValidator implements Validator<MultipartFile>, SizeValidator {

  private static final String MESSAGE_KEY_FILEUPLOAD_REQUIRED = "com.tallence.forms.fileupload.empty";
  private static final String MESSAGE_KEY_FILEUPLOAD_MIN = "com.tallence.forms.fileupload.min";
  private static final String MESSAGE_KEY_FILEUPLOAD_MAX = "com.tallence.forms.fileupload.max";

  private final FileUpload fileUpload;

  @ValidationProperty(messageKey = MESSAGE_KEY_FILEUPLOAD_REQUIRED)
  private boolean mandatory;

  //Default Value 5MB
  @ValidationProperty(messageKey = MESSAGE_KEY_FILEUPLOAD_MAX)
  private int maxSize = 5 * 1024;

  @ValidationProperty(messageKey = MESSAGE_KEY_FILEUPLOAD_MIN)
  private Integer minSize = 0;

  public FileUploadValidator(FileUpload fileUpload) {
    this.fileUpload = fileUpload;
  }

  @Override
  public List<ValidationFieldError> validate(MultipartFile value) {

    List<ValidationFieldError> errors = new ArrayList<>();

    boolean empty = value == null || value.getSize() == 0;
    if (this.mandatory && empty) {
      errors.add(new ValidationFieldError(MESSAGE_KEY_FILEUPLOAD_REQUIRED));
    }
    if (!empty && (value.getSize() / 1024) < this.minSize) {
      errors.add(new ValidationFieldError(MESSAGE_KEY_FILEUPLOAD_MIN, this.minSize));
    }
    if (!empty && (value.getSize() / 1024) > this.maxSize) {
      errors.add(new ValidationFieldError(MESSAGE_KEY_FILEUPLOAD_MAX, this.maxSize));
    }

    return errors;
  }

  @Override
  public boolean isMandatory() {
    return this.mandatory;
  }

  public void setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
  }

  @Override
  public Integer getMaxSize() {
    return this.maxSize;
  }

  @Override
  public Integer getMinSize() {
    return minSize;
  }

  public void setMinSize(Integer minSize) {
    this.minSize = minSize;
  }

  public void setMaxSize(Integer maxSize) {
    this.maxSize = maxSize;
  }
}
