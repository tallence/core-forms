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
import com.coremedia.cap.struct.Struct;
import com.coremedia.cap.struct.StructType;
import com.coremedia.cms.editor.sdk.premular.fields.struct.StructTreeNode;
import com.coremedia.cms.editor.sdk.premular.fields.struct.StructTreeStore;
import com.coremedia.ui.data.ValueExpression;
import com.coremedia.ui.data.ValueExpressionFactory;
import com.tallence.formeditor.studio.model.FormElementStructWrapper;

public class FormElementsManager {

  private var contentVE:ValueExpression;
  private var forceReadOnlyValueExpression:ValueExpression;
  private var collapsedElementVE:ValueExpression;
  private var dragActiveVE:ValueExpression;
  private var formDataStructPropertyName:String;
  private var formElementWrappersVE:ValueExpression;
  private var formElementsWrapperStore:StructTreeStore;
  private var formElementsStruct:StructTreeNode;

  public function FormElementsManager(contentVE:ValueExpression,
                                      forceReadOnlyValueExpression:ValueExpression,
                                      formDataStructPropertyName:String) {
    this.contentVE = contentVE;
    this.formDataStructPropertyName = formDataStructPropertyName;
    this.dragActiveVE = ValueExpressionFactory.createFromValue(false);
    this.forceReadOnlyValueExpression = forceReadOnlyValueExpression;
    initTreeStore();
  }

  public function getFormElementsVE():ValueExpression {
    if (!formElementWrappersVE) {
      formElementWrappersVE = ValueExpressionFactory.createFromValue([]);
    }
    return formElementWrappersVE;
  }

  public function addFormElement(afterFormElementId:String, formElementType:String):void {
    var initialData:Object = {
      validator: {},
      type: formElementType
    };

    var id:String = generateRandomId().toString();
    getRootNodeStruct().getType().addStructProperty(id, initialData);
    moveFormElement(afterFormElementId, id);

    //collapse all other FormElements and show the new one
    getCollapsedElementVE().setValue(id);
  }

  /**
   * Moves the struct of the given formElementId to the new position. The element is moved after the struct of the
   * given afterFormElementId.
   */
  public function moveFormElement(afterFormElementId:String, formElementId:String):void {
    if (formElementId != afterFormElementId) {
      var root:StructType = (formElementsWrapperStore.getRoot() as StructTreeNode).getValueAsStruct().getType();
      var formElementIds:Array = root.getPropertyNames();
      formElementIds.splice(formElementIds.indexOf(formElementId), 1);
      var position:Number = afterFormElementId != undefined ? formElementIds.indexOf(afterFormElementId) + 1 : 0;
      formElementIds.splice(position, 0, formElementId);
      root.rearrangeProperties(formElementIds);
    }
  }

  public function removeFormElement(elementId:String):void {
    getRootNodeStruct().getType().removeProperty(elementId);
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

  private function getRootNodeStruct():Struct {
    if (formElementsStruct) {
      return formElementsStruct.getValueAsStruct();
    } else {
      // if the sub struct is missing the sub struct has to be created
      var root:Struct = (formElementsWrapperStore.getRoot() as StructTreeNode).getValueAsStruct();
      root.getType().addStructProperty(FormElementStructWrapper.FORM_ELEMENTS_PROPERTY);
      return root.get(FormElementStructWrapper.FORM_ELEMENTS_PROPERTY);
    }
  }

  /**
   * Initializes the struct tree store. The store can be used to handle added or removed form elements, so that the
   * value expression evaluating to the list of applied form elements can be updated. A functional value expression can
   * not be used to evaluate the form elements directly, because the value expression would be evaluated too often.
   * Every time a property of a form element is changed, for example the name of the field, the value expression would
   * recalculate all form elements and the ui would be re-rendered.
   */
  private function initTreeStore():void {
    var storeConfig:StructTreeStore = StructTreeStore({});
    storeConfig.bindTo = contentVE;
    storeConfig.propertyName = formDataStructPropertyName;
    formElementsWrapperStore = new StructTreeStore(storeConfig);
    formElementsWrapperStore.addListener("nodeappend", nodeAppended);
    formElementsWrapperStore.addListener("nodeinsert", nodeAppended);
    formElementsWrapperStore.addListener("noderemove", nodeRemoved);
  }

  private function nodeRemoved(store:StructTreeStore, record:StructTreeNode):void {
    var depth:Number = record.getDepth();
    if (depth == 1) {
      // If the root node is removed another document form without a 'formElements' sub struct is opened. Therefore
      // the value expresison has to be set to an empty array
      formElementsStruct = null;
      getFormElementsVE().setValue([]);
    } else if (depth == 2) {
      updateFormElements();
    }
  }

  private function nodeAppended(store:StructTreeStore, record:StructTreeNode):void {
    var depth:Number = record.getDepth();
    if (depth == 1) {
      // The bind to value expression has changed due to a document form change. Therefor the new root struct has to be
      // set.
      formElementsStruct = record;
      updateFormElements();
    } else if (depth == 2) {
      updateFormElements();
    }
  }

  private function updateFormElements():void {
    var elements:Array = formElementsStruct.childNodes.map(function (node:StructTreeNode):FormElementStructWrapper {
      return new FormElementStructWrapper(
              node,
              formDataStructPropertyName,
              contentVE,
              forceReadOnlyValueExpression);
    });
    getFormElementsVE().setValue(elements);
  }

}
}
