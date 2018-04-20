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
import com.coremedia.cms.editor.sdk.editorContext;
import com.tallence.formeditor.studio.model.FormElementStructWrapper;

import ext.Ext;
import ext.container.Container;

public class ElementGroupBase extends Container {

  [Bindable]
  public var formElement:FormElementStructWrapper;

  protected var multipleDefaultValuesAllowed:Boolean;

  public function ElementGroupBase(config:ElementGroupBase = null) {
    super(config);
    multipleDefaultValuesAllowed = config.multipleDefaultValuesAllowed;
    formElement = config.formElement;
  }


/*
  TODO check if multiple defaultValues are set

  if (!multipleDefaultValuesAllowed && defaultValues.length > 0) {
    MessageBoxUtil.showWarn("Mehrere Default-Werte", "Nur für Check-Boxen können mehrere Default-Werte auf einmal verwendet werden. Bitte entfernen Sie zunächst die \"Default-Wert-Markierung\" an den anderen Elementen.");*/
  
}
}
