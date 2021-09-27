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
import com.tallence.formeditor.validator.ConsentFormCheckboxValidator;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Model bean for a configured Consent form checkBox.
 *
 */
public class ConsentFormCheckBox extends AbstractFormElement<Boolean, ConsentFormCheckboxValidator> {

  private Content linkTarget;

  public ConsentFormCheckBox() {
    super(Boolean.class);
  }

  public Content getLinkTarget() {
    return linkTarget;
  }

  public void setLinkTarget(Content linkTarget) {
    this.linkTarget = linkTarget;
  }

  @Override
  public void setValue(MultiValueMap<String, String> postData, HttpServletRequest request) {
    List<String> values = postData.get(getTechnicalName());
    if (values == null || values.isEmpty()) {
      setValue(null);
    } else if (values.size() == 1 && values.get(0).equals("on")) {
      setValue(true);
    } else {
      throw new IllegalStateException("Cannot parse value for " + getName() + " values: " + values);
    }
  }

  @Override
  public String serializeValue() {
    Boolean value = getValue();
    return value != null ? value.toString() : "";
  }
}
