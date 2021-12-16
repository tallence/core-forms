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
import ValueExpressionFactory from "@coremedia/studio-client.client-core/data/ValueExpressionFactory";
import Panel from "@jangaroo/ext-ts/panel/Panel";
import Config from "@jangaroo/runtime/Config";
import { AnyFunction } from "@jangaroo/runtime/types";
import FormEditor_properties from "../bundles/FormEditor_properties";
import EditOptionWindow from "../fields/EditOptionWindow";
import GroupElementStructWrapper from "../model/GroupElementStructWrapper";

interface ElementGroupEntryBaseConfig extends Config<Panel>, Partial<Pick<ElementGroupEntryBase,
  "removeGroupElementFn" |
  "updateOptionElementFn" |
  "moveOptionElementFn" |
  "option"
>> {
}

class ElementGroupEntryBase extends Panel {
  declare Config: ElementGroupEntryBaseConfig;

  #removeGroupElementFn: AnyFunction = null;

  get removeGroupElementFn(): AnyFunction {
    return this.#removeGroupElementFn;
  }

  set removeGroupElementFn(value: AnyFunction) {
    this.#removeGroupElementFn = value;
  }

  #updateOptionElementFn: AnyFunction = null;

  get updateOptionElementFn(): AnyFunction {
    return this.#updateOptionElementFn;
  }

  set updateOptionElementFn(value: AnyFunction) {
    this.#updateOptionElementFn = value;
  }

  #moveOptionElementFn: AnyFunction = null;

  get moveOptionElementFn(): AnyFunction {
    return this.#moveOptionElementFn;
  }

  set moveOptionElementFn(value: AnyFunction) {
    this.#moveOptionElementFn = value;
  }

  #option: GroupElementStructWrapper = null;

  get option(): GroupElementStructWrapper {
    return this.#option;
  }

  set option(value: GroupElementStructWrapper) {
    this.#option = value;
  }

  constructor(config: Config<ElementGroupEntryBase> = null) {
    super(config);
  }

  removeOption(): void {
    this.removeGroupElementFn.call(NaN, this.option.getId());
  }

  editOption(): void {
    const this$ = this;

    function onHandleSave(newId: string, newValue: string, isChecked: boolean): void {
      this$.updateOptionElementFn.call(NaN, this$.option, newId, newValue, isChecked);
    }

    function onHandleRemove(): void {
      this$.removeGroupElementFn.call(NaN, this$.option.getId());
    }

    const window = new EditOptionWindow(Config(EditOptionWindow, {
      width: 500,
      title: FormEditor_properties.FormEditor_text_edit_option + " '" + this.option.getId() + "'",
      onSaveCallback: onHandleSave,
      onRemoveCallback: onHandleRemove,
      option: this.option,
    }));

    window.show();
  }

  moveUp(): void {
    this.moveOptionElementFn.call(NaN, this.option.getId(), -1);
  }

  moveDown(): void {
    this.moveOptionElementFn.call(NaN, this.option.getId(), 1);
  }

  protected getPanelHeaderModifiers(option: GroupElementStructWrapper): ValueExpression {
    return ValueExpressionFactory.createFromFunction((): Array<any> => {
      const modifiers = [];
      if (option.getOptionSelectedByDefaultVE().getValue()) {
        modifiers.push("selected");
      }
      return modifiers;
    });
  }

}

export default ElementGroupEntryBase;
