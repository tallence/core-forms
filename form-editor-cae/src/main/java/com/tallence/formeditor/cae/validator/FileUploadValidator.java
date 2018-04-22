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

import com.tallence.formeditor.cae.annotations.Configured;
import com.tallence.formeditor.cae.elements.FileUpload;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Validator for elements of type {@link FileUpload}
 */
@Configured
public class FileUploadValidator implements Validator<MultipartFile> {

  private final FileUpload fileUpload;

  @Configured
  private boolean mandatory;

  @Configured
  //Default Value 5MB
  private int maxSize = 5 * 1024;

  private FileUploadValidator() {
    fileUpload = null;
  }

  public FileUploadValidator(FileUpload fileUpload) {
    this.fileUpload = fileUpload;
  }

  @Override
  public List<String> validate(MultipartFile value) {

    List<String> errors = new ArrayList<>();

    boolean empty = value == null || value.getSize() == 0;
    if (this.mandatory && empty) {
      errors.add("com.tallence.forms.fileupload.empty");
    } else if (!empty && (value.getSize() / 1024) > this.maxSize) {
      errors.add("com.tallence.forms.fileupload.toobig");
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

  public int getMaxSize() {
    return this.maxSize;
  }

  public void setMaxSize(int maxSize) {
    this.maxSize = maxSize;
  }
}
