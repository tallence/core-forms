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

import ContentPropertyNames from "@coremedia/studio-client.cap-rest-client/content/ContentPropertyNames";
import Struct from "@coremedia/studio-client.cap-rest-client/struct/Struct";
import PropertyPathExpression from "@coremedia/studio-client.client-core/data/PropertyPathExpression";
import ValueExpression from "@coremedia/studio-client.client-core/data/ValueExpression";
import StructTreeNode from "@coremedia/studio-client.main.editor-components/sdk/premular/fields/struct/StructTreeNode";
import { as } from "@jangaroo/runtime";
import AdvancedSettingsFieldBase from "../fields/advancedsettings/AdvancedSettingsFieldBase";

class FormElementStructWrapper {

  static readonly FORM_ELEMENTS_PROPERTY: string = "formElements";

  static readonly #TYPE_PROPERTY: string = "type";

  #structPropertyName: string = null;

  #id: string = null;

  #formElementVE: ValueExpression = null;

  #type: string = null;

  #bindTo: ValueExpression = null;

  #forceReadOnlyValueExpression: ValueExpression = null;

  #node: StructTreeNode = null;

  constructor(node: StructTreeNode,
    structPropertyName: string,
    bindTo: ValueExpression,
    forceReadOnlyValueExpression: ValueExpression) {
    this.#node = node;
    this.#structPropertyName = structPropertyName;
    this.#id = node.getPropertyName();
    this.#formElementVE = bindTo.extendBy(ContentPropertyNames.PROPERTIES, structPropertyName, FormElementStructWrapper.FORM_ELEMENTS_PROPERTY, this.#id);
    this.#type = FormElementStructWrapper.#getStructStringProperty(node.getValueAsStruct(), FormElementStructWrapper.#TYPE_PROPERTY);
    this.#bindTo = bindTo;
    this.#forceReadOnlyValueExpression = forceReadOnlyValueExpression;
  }

  getId(): string {
    return this.#node.getPropertyName();
  }

  getCustomId(): string {
    return FormElementStructWrapper.#getStructStringProperty(this.#getSubStruct("advancedSettings"), AdvancedSettingsFieldBase.CUSTOM_ID);
  }

  getFormElementVE(): ValueExpression {
    return this.#formElementVE;
  }

  getBindTo(): ValueExpression {
    return this.#bindTo;
  }

  getForceReadOnlyValueExpression(): ValueExpression {
    return this.#forceReadOnlyValueExpression;
  }

  getType(): string {
    return this.#type;
  }

  getNode(): StructTreeNode {
    return this.#node;
  }

  /**
   * Returns the property path of the applied form element.
   * e.g. 'formData.formElements.320798398'
   */
  getPropertyPath(): string {
    const path = as(this.getFormElementVE(), PropertyPathExpression).getPropertyPath();
    return path.replace("value.properties.", "");
  }

  #getSubStruct(propertyName: string): Struct {
    return this.#node.getValueAsStruct() && this.#node.getValueAsStruct().get(propertyName);
  }

  static #getStructStringProperty(struct: Struct, propertyName: string): string {
    return struct ? struct.get(propertyName) : "";
  }

}

export default FormElementStructWrapper;
