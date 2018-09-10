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
import com.coremedia.cap.struct.Struct;
import com.coremedia.ui.data.ValueExpression;

import ext.container.Container;

/**
 * Extend this class to create an editor for a field within a form element.
 */
public class FormEditorFieldBase extends Container {

  private var propertyVE:ValueExpression;

  public function FormEditorFieldBase(config:FormEditorField = null) {
    super(config);
    initFormEditorField(config);
  }

  /**
   * Initializes the editor by setting the value expression for the field and adding a value change listener. Since it
   * is not certain whether the constructor or getStructPropertyVE is called first, the initialization is called twice.
   */
  private function initFormEditorField(config:FormEditorField):void {
    if (!propertyVE) {
      var formElementStructVE:ValueExpression = config.formElementStructVE;
      formElementStructVE.addChangeListener(onFormElementStructChange);
      propertyVE = formElementStructVE.extendBy(config.propertyName);
    }
  }

  /**
   * Initializes the form editor field and returns the value expression that is evaluated for the property value of
   * the editor.
   */
  protected function getPropertyVE(config:FormEditorField):ValueExpression {
    initFormEditorField(config);
    return propertyVE;
  }

  /**
   * The method is called as soon as the struct of the form element changes. The value can change when a new form
   * element is selected or added, or when the struct changes. The initStruct method is called so that the editor can
   * ensure that the struct has the correct structure.
   * @param structVE
   */
  private function onFormElementStructChange(structVE:ValueExpression):void {
    var struct:Struct = structVE.getValue();
    if (struct) {
      initStruct(struct);
    }
    if (propertyVE.getValue() == undefined) {
      initWithDefault(propertyVE);
    }
    propertyVE.addChangeListener(function (ve:ValueExpression):void {
      if (ve.getValue() == undefined) {
        initWithDefault(ve);
      }
    })
  }

  /**
   * Override this method to set a default value if the entry in the struct is still undefined. For strings or integer
   * values, the values can be written directly into the value expression.
   */
  protected function initWithDefault(ve:ValueExpression):void {
  }

  /**
   * Override this method to perform operations directly on the struct. The struct contains the values of the form
   * element. If the editor for the field inside the form element must store values in a sub-struct, the struct can be
   * initialized by this method.
   */
  protected function initStruct(struct:Struct):void {
  }
}
}