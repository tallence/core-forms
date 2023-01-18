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

import ValueExpression from "@coremedia/studio-client.client-core/data/ValueExpression";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import FormEditorField from "../fields/FormEditorField";
import FormElementStructWrapper from "../model/FormElementStructWrapper";
import AbstractFormElementBase from "./AbstractFormElementBase";

interface AbstractFormElementConfig extends Config<AbstractFormElementBase>, Partial<Pick<AbstractFormElement,
  "bindTo" |
  "forceReadOnlyValueExpression" |
  "formElementType" |
  "formElementIconCls" |
  "formElementGroup" |
  "formElementEditorItems" |
  "formElement"
>> {
}

class AbstractFormElement extends AbstractFormElementBase {
  declare Config: AbstractFormElementConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.elements.abstractFormElement";

  #bindTo: ValueExpression = null;

  get bindTo(): ValueExpression {
    return this.#bindTo;
  }

  set bindTo(value: ValueExpression) {
    this.#bindTo = value;
  }

  #forceReadOnlyValueExpression: ValueExpression = null;

  get forceReadOnlyValueExpression(): ValueExpression {
    return this.#forceReadOnlyValueExpression;
  }

  set forceReadOnlyValueExpression(value: ValueExpression) {
    this.#forceReadOnlyValueExpression = value;
  }

  #formElementType: string = null;

  get formElementType(): string {
    return this.#formElementType;
  }

  set formElementType(value: string) {
    this.#formElementType = value;
  }

  #formElementIconCls: string = null;

  get formElementIconCls(): string {
    return this.#formElementIconCls;
  }

  set formElementIconCls(value: string) {
    this.#formElementIconCls = value;
  }

  #formElementGroup: string = null;

  get formElementGroup(): string {
    return this.#formElementGroup;
  }

  set formElementGroup(value: string) {
    this.#formElementGroup = value;
  }

  #formElementEditorItems: Array<any> = null;

  get formElementEditorItems(): Array<any> {
    return this.#formElementEditorItems;
  }

  set formElementEditorItems(value: Array<any>) {
    this.#formElementEditorItems = value;
  }

  #formElement: FormElementStructWrapper = null;

  get formElement(): FormElementStructWrapper {
    return this.#formElement;
  }

  set formElement(value: FormElementStructWrapper) {
    this.#formElement = value;
  }

  constructor(config: Config<AbstractFormElement> = null) {
    super((()=> ConfigUtils.apply(Config(AbstractFormElement, {
      defaultType: FormEditorField.xtype,
      defaults: Config<FormEditorField>({
        bindTo: this.getBindTo(),
        forceReadOnlyValueExpression: this.getForceReadOnlyVE(),
        formElementStructVE: this.getFormElementStructVE(),
        propertyPathVE: this.getPropertyPathVE(),
        formIssuesVE: this.getFormIssuesVE(),
      }),

    }), config))());
  }
}

export default AbstractFormElement;
