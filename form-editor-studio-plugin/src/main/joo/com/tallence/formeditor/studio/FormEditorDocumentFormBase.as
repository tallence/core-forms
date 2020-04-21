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
import com.coremedia.ui.util.IReusableComponentsService;
import com.coremedia.ui.util.ReusableComponentsServiceImpl;
import com.tallence.formeditor.studio.elements.AbstractFormElement;
import com.tallence.formeditor.studio.helper.FormElementsManager;

import ext.ComponentManager;

public class FormEditorDocumentFormBase extends DocumentForm {

  private var formElementsManager:FormElementsManager;

  public function FormEditorDocumentFormBase(config:FormEditorDocumentForm = null) {
    super(config);
  }

  /**
   * Each editor for a form element is reused. In order to use the
   * {@link com.coremedia.ui.util.IReusableComponentsService}, the component must first be created by the
   * {@link ext.ComponentManager}. Then the form editor component is registered and can be used afterwards.
   */
  protected static function initReusableComponents(formElements:Array):void {
    var reusableComponentsSerice:IReusableComponentsService = ReusableComponentsServiceImpl.getInstance();
    for (var i:int = 0; i < formElements.length; i++) {
      var formElement:AbstractFormElement = AbstractFormElement(ComponentManager.create(formElements[i]));

      var key:String = formElement.getFormElementType();
      if (!reusableComponentsSerice.isReusabilityEnabled(key)) {
        reusableComponentsSerice.setReusabilityLimit(key, 1);
        reusableComponentsSerice.registerComponentForReuse(key, formElement);
      }
    }
  }

  protected function getFormElementsManager(bindTo:ValueExpression,
                                            forceReadOnlyValueExpression:ValueExpression,
                                            propertyName:String):FormElementsManager {
    if (!formElementsManager) {
      formElementsManager = new FormElementsManager(bindTo, forceReadOnlyValueExpression, propertyName);
    }
    return formElementsManager;
  }

}
}
