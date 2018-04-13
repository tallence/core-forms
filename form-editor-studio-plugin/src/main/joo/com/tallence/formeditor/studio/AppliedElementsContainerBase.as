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
import com.coremedia.ui.data.ValueExpression;
import com.coremedia.ui.data.ValueExpressionFactory;
import com.tallence.formeditor.studio.helper.FormElementsManager;

import ext.container.Container;

public class AppliedElementsContainerBase extends Container {

  private var formElementsManager:FormElementsManager;

  public function AppliedElementsContainerBase(config:AppliedElementsContainer = null) {
    super(config);
  }

  protected function getFormElementsManager(bindTo:ValueExpression, propertyName:String, dragActiveVE:ValueExpression):FormElementsManager {
    if (!formElementsManager) {
      formElementsManager = new FormElementsManager(bindTo, propertyName, dragActiveVE);
    }
    return formElementsManager;
  }

  protected function getFormElementsVE(bindTo:ValueExpression, propertyName:String, dragActiveVE:ValueExpression):ValueExpression {
    return ValueExpressionFactory.createFromFunction(function ():Array {
      return getFormElementsManager(bindTo, propertyName, dragActiveVE).getFormElements();
    });
  }

}
}
