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
package com.tallence.formeditor.cae.mocks;

import com.tallence.formeditor.cae.actions.FormEditorStorageAdapter;
import com.tallence.formeditor.cae.elements.FormElement;
import com.tallence.formeditor.contentbeans.FormEditor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Mocking the {@link FormEditorStorageAdapter} and storing the params in local variables,
 * which can be checked by tests afterwards.
 *
 */
@Component
public class StorageAdapterMock implements FormEditorStorageAdapter {

  public String formData;

  @Override
  public boolean persistFormData(FormEditor target, String formData, List<FormElement> elements, List<MultipartFile> files) {

    if (this.formData != null) {
      throw new IllegalStateException("Call com.tallence.formeditor.cae.mocks.StorageAdapterMock.clear before setting form data again");
    }

    this.formData = formData;
    return true;
  }

  public void clear() {
    this.formData = null;
  }
}
