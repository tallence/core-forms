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
import com.tallence.formeditor.validator.UsersMailValidator;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;

/**
 * Model bean for a configured Field for user's mail.
 * It holds two values: one for the mail-address and one for a config, which controls the mail-sending behaviour,
 * see {@link CopyBoxOption}
 *
 */
public class UsersMail extends AbstractFormElement<UsersMail.UsersMailData, UsersMailValidator> {

  private CopyBoxOption copyBoxOption;

  public UsersMail(Content formEditor) {
    super(UsersMailData.class, formEditor);
  }


  /**
   * returns the name for the Send-Copy-CheckBox
   */
  public String getSendCopyName() {
    return getTechnicalName() + "_sendCopy";
  }

  public boolean isDisplayCheckbox() {
    return CopyBoxOption.CHECKBOX.equals(this.copyBoxOption);
  }


  @Override
  public String serializeValue() {
    if (getValue() != null) {
      return getValue().getUsersMail();
    }
    return "";
  }


  @Override
  public void setValue(MultiValueMap<String, String> postData, HttpServletRequest request) {
    String mail = postData.getFirst(getTechnicalName());
    if (mail != null) {

      //Send a mail, if the checkBox is activated or if the mail is to be sent always
      boolean sendCopy = isDisplayCheckbox() && getSendCopyName().equals(postData.getFirst(getSendCopyName()))
          || CopyBoxOption.ALWAYS.equals(this.copyBoxOption);

      UsersMail.UsersMailData data = new UsersMailData(mail, sendCopy);
      setValue(data);
    }
  }

  public CopyBoxOption getCopyBoxOption() {
    return this.copyBoxOption;
  }

  public void setCopyBoxOption(CopyBoxOption copyBoxOption) {
    this.copyBoxOption = copyBoxOption;
  }

  public enum CopyBoxOption {
    CHECKBOX, NEVER, ALWAYS
  }

  public class UsersMailData {
    private final String usersMail;
    private final boolean sendCopy;

    public UsersMailData(String usersMail, boolean sendCopy) {
      this.usersMail = usersMail;
      this.sendCopy = sendCopy;
    }

    public String getUsersMail() {
      return this.usersMail;
    }

    public boolean isSendCopy() {
      return this.sendCopy;
    }
  }
}
