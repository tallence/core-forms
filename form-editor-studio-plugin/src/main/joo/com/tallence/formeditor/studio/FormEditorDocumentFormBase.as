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

package com.tallence.formeditor.studio {
import com.coremedia.cms.editor.sdk.premular.DocumentForm;
import com.coremedia.ui.data.ValueExpression;
import com.coremedia.ui.data.ValueExpressionFactory;
import com.coremedia.ui.util.ReusableComponentsServiceImpl;
import com.tallence.formeditor.studio.elements.AbstractFormElement;

import ext.ComponentManager;

public class FormEditorDocumentFormBase extends DocumentForm {

  private var dragActiveVE:ValueExpression;

  public function FormEditorDocumentFormBase(config:FormEditorDocumentForm = null) {
    super(config);
  }

  /**
   * Stores the information whether a drag and drop operation is in progress. The ValueExpression must be created in
   * the document form, because the drag/drop state must also be updated for the applicable elements.
   */
  protected function getDragActiveVE():ValueExpression {
    if (!dragActiveVE) {
      dragActiveVE = ValueExpressionFactory.createFromValue(false);
    }
    return dragActiveVE;
  }

  /**
   * Each editor for a form element is reused. In order to use the
   * {@link com.coremedia.ui.util.IReusableComponentsService}, the component must first be created by the
   * {@link ext.ComponentManager}. Then the form editor component is registered and can be used afterwards.
   */
  protected static function initReusableComponents(formElements:Array):void {
    for (var i:int = 0; i < formElements.length; i++) {
      var formElement:AbstractFormElement = AbstractFormElement(ComponentManager.create(formElements[i]));
      ReusableComponentsServiceImpl.getInstance().setReusabilityLimit(formElement.getFormElementType(), 1);
      ReusableComponentsServiceImpl.getInstance().registerComponentForReuse(formElement.getFormElementType(), formElement);
    }
  }

}
}