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

package com.tallence.formeditor.elements;

import com.coremedia.cap.content.Content;
import com.tallence.formeditor.validator.FileUploadValidator;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Model bean for a configured FileUpload-Field.
 */
public class FileUpload extends AbstractFormElement<List, FileUploadValidator> {

  public static String ALLOW_MULTIPLE_UPLOADS = "allowMultipleUploads";

  public FileUpload(Content formEditor) {
    super(List.class, formEditor);
  }

  private List<String> options;
  private boolean allowMultipleUploads;


  @Override
  public String serializeValue() {
    throw new IllegalStateException("Serializing a FileUpload is not implemented!");
  }


  @Override
  public void setValue(MultiValueMap<String, String> postData, HttpServletRequest request) {
    throw new IllegalStateException("PostData for Files has to be parsed in Controller!");
  }


  @Override
  public void fillFormData(Map<String, String> formData) {
    //Do nothing, no formData needed for FileUpload
  }

  public List<String> getOptions() {
    return this.options;
  }

  public void setOptions(List<String> options) {
    this.options = options;
  }

  public boolean getAllowMultipleUploads() {
    return allowMultipleUploads;
  }

  public void setAllowMultipleUploads(boolean allowMultipleUploads) {
    this.allowMultipleUploads = allowMultipleUploads;
  }

  /**
   * Custom method to return checked list of MultipartFile items
   */
  public List<MultipartFile> getFiles() {
    List<MultipartFile> files = new ArrayList<>();
    List list = getValue();
    for (Object file : list) {
      if (file instanceof MultipartFile) {
        files.add((MultipartFile) file);
      } else {
        throw new IllegalArgumentException("File item is not instance of MultipartFile: " + file.getClass().getName());
      }
    }
    return files;
  }
}
