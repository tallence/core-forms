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

package com.tallence.formeditor.studio.fields {
import com.coremedia.cap.common.SESSION;
import com.coremedia.cap.content.ContentType;
import com.coremedia.cap.struct.StructType;
import com.coremedia.ui.data.ValueExpression;
import com.coremedia.ui.data.ValueExpressionFactory;
import com.tallence.formeditor.studio.model.FormElementStructWrapper;

import ext.container.Container;

public class LinkedContentFieldBase extends Container {

  private var linkTargetVE:ValueExpression;

  public function LinkedContentFieldBase(config:LinkedContentField = null) {
    super(config);
  }

  /**
   * Creates the {@link ValueExpression} for the linked content. The value expression needs to be created
   * with the createFromFunction method. Within that function the value expression can be wrapped
   * in a {@link com.coremedia.ui.data.ValueHolder}. After the deletion of the form element, the ValueExpression for the
   * linked content will be undefined. In that case the {@link SingleLinkValueExpressionHolder} will return an empty array,
   * otherwise the single link editor will throw an error.
   * @param config
   * @return
   */
  protected function getLinkTargetVE(config:LinkedContentField):ValueExpression {
    return ValueExpressionFactory.createFromFunction(function ():* {
      if (!linkTargetVE) {
        linkTargetVE = config.formElement.getFormElementVE().extendBy(config.propertyname);
        if (!linkTargetVE.getValue()) {
          initStruct(config);
        }
      }
      return new SingleLinkValueExpressionHolder(linkTargetVE);
    });
  }

  private static function initStruct(config:LinkedContentField):void {
    var formElement:FormElementStructWrapper = config.formElement;
    var contentType:ContentType = SESSION.getConnection().getContentRepository().getContentType(config.linkContentType);
    var formElementsStruct:StructType = formElement.getFormElementVE().getValue().getType();
    formElementsStruct.addLinkListProperty(config.propertyname, contentType);
  }

}
}