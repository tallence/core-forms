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
import com.coremedia.cap.struct.Struct;
import com.coremedia.cap.struct.StructType;
import com.coremedia.ui.data.ValueExpression;
import com.coremedia.ui.data.ValueExpressionFactory;

public class LinkedContentFieldBase extends FormEditorField {

  protected static const DEFAULT_LINK_CONTENT_TYPE:String = "CMLinkable";

  private var linkTargetVE:ValueExpression;

  [Bindable]
  public var linkContentType:String;

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
        linkTargetVE = getPropertyVE(config);
      }
      return new SingleLinkValueExpressionHolder(linkTargetVE);
    });
  }

  /**
   * Initializes the struct for the form element and and adds a link list property to the struct. The link list
   * property field can not automatically add this property.
   * @param struct
   */
  override protected function initStruct(struct:Struct):void {
    var contentType:ContentType = SESSION.getConnection().getContentRepository().getContentType(linkContentType ? linkContentType : DEFAULT_LINK_CONTENT_TYPE);
    var formElementsStruct:StructType = struct.getType();
    formElementsStruct.addLinkListProperty(propertyName, contentType);
  }

}
}
