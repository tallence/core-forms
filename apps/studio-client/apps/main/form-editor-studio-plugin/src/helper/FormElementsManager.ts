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

import Struct from "@coremedia/studio-client.cap-rest-client/struct/Struct";
import ValueExpression from "@coremedia/studio-client.client-core/data/ValueExpression";
import ValueExpressionFactory from "@coremedia/studio-client.client-core/data/ValueExpressionFactory";
import StructTreeNode from "@coremedia/studio-client.main.editor-components/sdk/premular/fields/struct/StructTreeNode";
import StructTreeStore
  from "@coremedia/studio-client.main.editor-components/sdk/premular/fields/struct/StructTreeStore";
import {as, bind} from "@jangaroo/runtime";
import Config from "@jangaroo/runtime/Config";
import int from "@jangaroo/runtime/int";
import FormElementStructWrapper from "../model/FormElementStructWrapper";
import NodeInterface from "@jangaroo/ext-ts/data/NodeInterface";
import PageElementEditor from "../elements/PageElementEditor";
import FormEditor_properties from "../bundles/FormEditor_properties";

class FormElementsManager {

  #contentVE: ValueExpression = null;

  #forceReadOnlyValueExpression: ValueExpression = null;

  #collapsedElementVE: ValueExpression = null;

  #dragActiveVE: ValueExpression = null;

  #formDataStructPropertyName: string = null;

  #formElementWrappersVE: ValueExpression = null;

  #formElementsWrapperStore: StructTreeStore = null;

  #formElementsStruct: StructTreeNode = null;

  constructor(contentVE: ValueExpression,
              forceReadOnlyValueExpression: ValueExpression,
              formDataStructPropertyName: string) {
    this.#contentVE = contentVE;
    this.#formDataStructPropertyName = formDataStructPropertyName;
    this.#dragActiveVE = ValueExpressionFactory.createFromValue(false);
    this.#forceReadOnlyValueExpression = forceReadOnlyValueExpression;
    this.#initTreeStore();
  }

  getFormElementsVE(): ValueExpression {
    if (!this.#formElementWrappersVE) {
      this.#formElementWrappersVE = ValueExpressionFactory.createFromValue([]);
    }
    return this.#formElementWrappersVE;
  }

  static getPageInitialData(title: string): Record<string, any> {
    return {
      name: title,
      type: PageElementEditor.FIELD_TYPE,
      pageType: PageElementEditor.DEFAULT_PAGE,
      validator: {},
      formElements: {}
    };

  }

  addFormPage(afterFormElementId: string): void {
    this.addElement(afterFormElementId, FormElementsManager.getPageInitialData(FormEditor_properties.FormEditor_pages_new_title));
  }

  addFormElement(afterFormElementId: string, formElementType: string): void {
    const initialData: Record<string, any> = {
      validator: {},
      type: formElementType,
    };

    this.addElement(afterFormElementId, initialData);
  }

  addElement(afterFormElementId: string, initialData: Record<string, any>): void {
    const id = FormElementsManager.generateRandomId().toString();
    this.#getRootNodeStruct().getType().addStructProperty(id, initialData);
    this.moveFormElement(afterFormElementId, id);

    //collapse all other FormElements and show the new one
    this.getCollapsedElementVE().setValue(id);
  }

  /**
   * Moves the struct of the given formElementId to the new position. The element is moved after the struct of the
   * given afterFormElementId.
   */
  moveFormElement(afterFormElementId: string, formElementId: string): void {
    if (formElementId != afterFormElementId) {
      const formElements = this.#getRootNodeStruct().getType();
      const formElementIds = formElements.getPropertyNames();
      formElementIds.splice(formElementIds.indexOf(formElementId), 1);
      const position: number = afterFormElementId != undefined ? formElementIds.indexOf(afterFormElementId) + 1 : 0;
      formElementIds.splice(position, 0, formElementId);
      formElements.rearrangeProperties(formElementIds);
    }
  }

  removeFormElement(elementId: string): void {
    this.#getRootNodeStruct().getType().removeProperty(elementId);
  }

  static generateRandomId(): number {
    return Math.floor(Math.random() * (int.MAX_VALUE - 0 + 1)) + 23;
  }

  getCollapsedElementVE(): ValueExpression {
    if (!this.#collapsedElementVE) {
      this.#collapsedElementVE = ValueExpressionFactory.createFromValue("");
    }
    return this.#collapsedElementVE;
  }

  /**
   * Stores the information whether a drag and drop operation is in progress.
   */
  getDragActiveVE(): ValueExpression {
    return this.#dragActiveVE;
  }

  #getRootNodeStruct(): Struct {
    if (this.#formElementsStruct) {
      return this.#formElementsStruct.getValueAsStruct();
    } else {
      // if the sub struct is missing the sub struct has to be created
      let structTreeNode = as(this.#formElementsWrapperStore.getRoot(), StructTreeNode);
      if (structTreeNode.isInstance) {
        const root = structTreeNode.getValueAsStruct();
        root.getType().addStructProperty(FormElementStructWrapper.FORM_ELEMENTS_PROPERTY);
        return root.get(FormElementStructWrapper.FORM_ELEMENTS_PROPERTY);
      }
    }
  }

  /**
   * Initializes the struct tree store. The store can be used to handle added or removed form elements, so that the
   * value expression evaluating to the list of applied form elements can be updated. A functional value expression can
   * not be used to evaluate the form elements directly, because the value expression would be evaluated too often.
   * Every time a property of a form element is changed, for example the name of the field, the value expression would
   * recalculate all form elements and the ui would be re-rendered.
   */
  #initTreeStore(): void {
    const storeConfig = Config(StructTreeStore);
    storeConfig.bindTo = this.#contentVE;
    storeConfig.propertyName = this.#formDataStructPropertyName;
    this.#formElementsWrapperStore = new StructTreeStore(storeConfig);
    this.#formElementsWrapperStore.addListener("nodeappend", bind(this, this.#nodeAppended));
    this.#formElementsWrapperStore.addListener("nodeinsert", bind(this, this.#nodeInserted));
    this.#formElementsWrapperStore.addListener("noderemove", bind(this, this.#nodeRemoved));
    let formElements = this.#formElementsWrapperStore.getRoot().findChild("text", FormElementStructWrapper.FORM_ELEMENTS_PROPERTY);
    if (formElements != null) {
      this.#formElementsStruct = formElements;
      this.#updateFormElements();
    }

  }

  #nodeRemoved(_store: NodeInterface, record: NodeInterface): void {
    const depth = record.getDepth();
    if (depth == 1) {
      // If the root node is removed another document form without a 'formElements' sub struct is opened. Therefore
      // the value expresison has to be set to an empty array
      this.#formElementsStruct = null;
      this.getFormElementsVE().setValue([]);
    } else if (depth == 2) {
      this.#updateFormElements();
    }
  }

  #nodeInserted(store: NodeInterface, record: NodeInterface, _refNode: NodeInterface): any {
    return this.#addNodeInternal(store, record);
  }

  #nodeAppended(store: NodeInterface, record: NodeInterface, _index: number): any {
    return this.#addNodeInternal(store, record);
  }

  #addNodeInternal(_store: NodeInterface, record: NodeInterface): any {
    const depth = record.getDepth();
    if (depth == 1 && record instanceof StructTreeNode) {
      // The bind to value expression has changed due to a document form change. Therefor the new root struct has to be
      // set.
      this.#formElementsStruct = record;
      this.#updateFormElements();
    } else if (depth == 2) {
      this.#updateFormElements();
    }
  }

  #updateFormElements(): void {
    const elements = this.generateWrapper(this.#formElementsStruct.childNodes);
    this.getFormElementsVE().setValue(elements);
  }

  generateWrapper(childNodes: NodeInterface[]): Array<FormElementStructWrapper> {
    return childNodes.map((node: StructTreeNode): FormElementStructWrapper =>
            new FormElementStructWrapper(
                    node,
                    this.#formDataStructPropertyName,
                    this.#contentVE,
                    this.#forceReadOnlyValueExpression)
    );
  }

}

export default FormElementsManager;
