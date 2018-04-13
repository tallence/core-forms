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

package com.tallence.formeditor.studio.helper {
import com.coremedia.cap.common.impl.StructSubBean;
import com.coremedia.cap.content.impl.ContentStructRemoteBeanImpl;
import com.coremedia.cap.struct.Struct;
import com.coremedia.cap.struct.StructType;
import com.coremedia.ui.data.ValueExpression;
import com.coremedia.ui.data.ValueExpressionFactory;
import com.tallence.formeditor.studio.model.FormElementStructWrapper;

import ext.Ext;

public class FormElementsManager {

  private static const FORM_ELEMENTS_PROPERTY:String = "formElements";
  private static const PROPERTIES:String = 'properties';

  private var contentVE:ValueExpression;
  private var collapsedElementVE:ValueExpression;
  private var dragActiveVE:ValueExpression;
  private var formDataStructPropertyName:String;
  private var formElementsStructVE:ValueExpression;

  public function FormElementsManager(contentVE:ValueExpression, formDataStructPropertyName:String, dragActiveVE:ValueExpression) {
    this.contentVE = contentVE;
    this.formDataStructPropertyName = formDataStructPropertyName;
    this.dragActiveVE = dragActiveVE;
  }

  public function addFormElement(afterFormElementId:String, formElementType:String):void {
    var remoteStruct:ContentStructRemoteBeanImpl = getFormElementsStructVE().getValue();
    remoteStruct.load(function (formElementsStruct:Struct):void {
      if (!formElementsStruct.get(FORM_ELEMENTS_PROPERTY)) {
        formElementsStruct.getType().addStructProperty(FORM_ELEMENTS_PROPERTY);
      }

      var formElements:StructType = formElementsStruct.get(FORM_ELEMENTS_PROPERTY).getType();

      var initialData:Object = {
        validator: {},
        type: formElementType
      };

      var id:String = generateRandomId().toString();
      formElements.addStructProperty(id, initialData);
      rearrangeProperties(formElementsStruct, afterFormElementId, id);

      //collapse all other FormElements and show the new one
      getCollapsedElementVE().setValue(id);
    });
  }

  public function moveFormElement(afterFormElementId:String, formElementId:String):void {
    var remoteStruct:ContentStructRemoteBeanImpl = getFormElementsStructVE().getValue();
    remoteStruct.load(function (formElementsStruct:Struct):void {
      rearrangeProperties(formElementsStruct, afterFormElementId, formElementId);
    });
  }

  /**
   * Moves the struct of the given formElementId to the new position. The element is moved after the struct of the given afterFormElementId.
   */
  private static function rearrangeProperties(formElementsStruct:Struct, afterFormElementId:String, formElementId:String):void {
    if (formElementId != afterFormElementId) {
      var formElements:StructType = formElementsStruct.get(FORM_ELEMENTS_PROPERTY).getType();
      var formElementIds:Array = formElements.getPropertyNames();
      formElementIds.splice(formElementIds.indexOf(formElementId), 1);
      var position:Number = afterFormElementId != undefined ? formElementIds.indexOf(afterFormElementId) + 1 : 0;
      formElementIds.splice(position, 0, formElementId);
      formElements.rearrangeProperties(formElementIds);
    }
  }

  public function getFormElements():Array {
    var elements:Array = [];
    if (!getFormElementsStructVE().isLoaded()) {
      getFormElementsStructVE().loadValue(Ext.emptyFn);
      return undefined;
    }
    var formStruct:Struct = getFormElementsStructVE().getValue();
    if (formStruct.get(FORM_ELEMENTS_PROPERTY)) {
      var formElementsStruct:Struct = formStruct.get(FORM_ELEMENTS_PROPERTY) as Struct;
      elements = formElementsStruct.getType().getPropertyNames().map(function (id:String):FormElementStructWrapper {
        return new FormElementStructWrapper(StructSubBean(formElementsStruct.get(id)), id, getFormElementsStructVE().extendBy(FORM_ELEMENTS_PROPERTY + "." + id));
      });
    }
    return elements;
  }

  public function removeFormElement(elementId:String):void {
    getFormElementsStructVE().loadValue(function (formElementsStruct:Struct):void {
      (formElementsStruct.get(FORM_ELEMENTS_PROPERTY) as Struct).getType().removeProperty(elementId);
    });
  }

  public function getFormElementsStructVE():ValueExpression {
    if (!formElementsStructVE) {
      formElementsStructVE = contentVE.extendBy(PROPERTIES, formDataStructPropertyName);
    }
    return formElementsStructVE;
  }

  private static function generateRandomId():Number {
    return Math.floor(Math.random() * (int.MAX_VALUE - 0 + 1)) + 23;
  }

  public function getCollapsedElementVE():ValueExpression {
    if (!collapsedElementVE) {
      collapsedElementVE = ValueExpressionFactory.createFromValue("");
    }
    return collapsedElementVE;
  }

  /**
   * Stores the information whether a drag and drop operation is in progress.
   */
  public function getDragActiveVE():ValueExpression {
    return dragActiveVE;
  }

}
}