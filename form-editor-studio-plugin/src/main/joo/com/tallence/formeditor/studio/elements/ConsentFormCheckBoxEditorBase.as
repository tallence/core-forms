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

package com.tallence.formeditor.studio.elements {
import com.coremedia.cap.common.SESSION;
import com.coremedia.cap.content.ContentType;
import com.coremedia.cap.struct.StructType;
import com.coremedia.ui.data.ValueExpression;
import com.tallence.formeditor.studio.model.FormElementStructWrapper;

import ext.container.Container;

public class ConsentFormCheckBoxEditorBase extends Container {

  private var cmTeasableContentType:ContentType = SESSION.getConnection().getContentRepository().getContentType("CMTeasable");

  [Bindable]
  public var formElement:FormElementStructWrapper;

  private var linkTargetVE: ValueExpression;

  public function ConsentFormCheckBoxEditorBase(config:ConsentFormCheckBoxEditorBase = null) {
    super(config);
    formElement = config.formElement;
  }

  protected function getLinkTargetVE(formElement:FormElementStructWrapper): ValueExpression {
    if (!linkTargetVE) {
      //this.linkTargetVE = ValueExpressionFactory.createFromValue([]);
      this.linkTargetVE = formElement.getFormElementVE().extendBy('linkTarget');
      if (!this.linkTargetVE.getValue()) {
        var formElementsStruct:StructType = formElement.getFormElementVE().getValue().getType();
        formElementsStruct.addLinkListProperty("linkTarget", cmTeasableContentType);
      }
    }
    return this.linkTargetVE;
  }
}
}
