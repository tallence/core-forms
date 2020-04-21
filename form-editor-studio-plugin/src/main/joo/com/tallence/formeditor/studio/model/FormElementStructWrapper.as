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

package com.tallence.formeditor.studio.model {
import com.coremedia.cap.content.ContentPropertyNames;
import com.coremedia.cap.struct.Struct;
import com.coremedia.cms.editor.sdk.premular.fields.struct.StructTreeNode;
import com.coremedia.ui.data.PropertyPathExpression;
import com.coremedia.ui.data.ValueExpression;
import com.tallence.formeditor.studio.fields.advancedsettings.AdvancedSettingsFieldBase;

public class FormElementStructWrapper {

  public static const FORM_ELEMENTS_PROPERTY:String = "formElements";
  private static const TYPE_PROPERTY:String = "type";

  private var structPropertyName:String;
  private var id:String;
  private var formElementVE:ValueExpression;
  private var type:String;
  private var bindTo:ValueExpression;
  private var forceReadOnlyValueExpression:ValueExpression;
  private var node:StructTreeNode;

  public function FormElementStructWrapper(node:StructTreeNode,
                                           structPropertyName:String,
                                           bindTo:ValueExpression,
                                           forceReadOnlyValueExpression:ValueExpression) {
    this.node = node;
    this.structPropertyName = structPropertyName;
    this.id = node.getPropertyName();
    this.formElementVE = bindTo.extendBy(ContentPropertyNames.PROPERTIES, structPropertyName, FORM_ELEMENTS_PROPERTY, this.id);
    this.type = getStructStringProperty(node.getValueAsStruct(), TYPE_PROPERTY);
    this.bindTo = bindTo;
    this.forceReadOnlyValueExpression = forceReadOnlyValueExpression;
  }

  public function getId():String {
    return node.getPropertyName();
  }

  public function getCustomId():String {
    return getStructStringProperty(getSubStruct("advancedSettings"), AdvancedSettingsFieldBase.CUSTOM_ID);
  }

  public function getFormElementVE():ValueExpression {
    return formElementVE;
  }

  public function getBindTo():ValueExpression {
    return bindTo;
  }

  public function getForceReadOnlyValueExpression():ValueExpression {
    return forceReadOnlyValueExpression;
  }

  public function getType():String {
    return type;
  }

  /**
   * Returns the property path of the applied form element.
   * e.g. 'formData.formElements.320798398'
   */
  public function getPropertyPath():String {
    var path:String = (getFormElementVE() as PropertyPathExpression).getPropertyPath();
    return path.replace("value.properties.", "");
  }

  private function getSubStruct(propertyName:String):Struct {
    return node.getValueAsStruct() && node.getValueAsStruct().get(propertyName);
  }

  private static function getStructStringProperty(struct:Struct, propertyName:String):String {
    return struct ? struct.get(propertyName) : "";
  }

}
}
