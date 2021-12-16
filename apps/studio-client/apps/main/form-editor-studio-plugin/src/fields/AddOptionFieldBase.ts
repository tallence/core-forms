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
import Format from "@jangaroo/ext-ts/util/Format";
import MessageBoxWindow from "@jangaroo/ext-ts/window/MessageBox";
import { bind } from "@jangaroo/runtime";
import Config from "@jangaroo/runtime/Config";
import FormUtils from "../FormUtils";
import GroupElementStructWrapper from "../model/GroupElementStructWrapper";
import AddOptionField from "./AddOptionField";
import FormEditorField from "./FormEditorField";

interface AddOptionFieldBaseConfig extends Config<FormEditorField>, Partial<Pick<AddOptionFieldBase,
  "allowMultiDefaultSelection"
>> {
}

class AddOptionFieldBase extends FormEditorField {
  declare Config: AddOptionFieldBaseConfig;

  #allowMultiDefaultSelection: boolean = false;

  get allowMultiDefaultSelection(): boolean {
    return this.#allowMultiDefaultSelection;
  }

  set allowMultiDefaultSelection(value: boolean) {
    this.#allowMultiDefaultSelection = value;
  }

  #addOptionVE: ValueExpression = null;

  #formElementsStruct: Struct = null;

  #groupElementStructName: string = null;

  constructor(config: Config<AddOptionField> = null) {
    super(config);
    this.#groupElementStructName = config.propertyName;
  }

  addGroupElement(): void {

    const newOptionText: string = this.#addOptionVE.getValue();
    if (newOptionText) {

      if (!this.#formElementsStruct.get(this.#groupElementStructName)) {
        this.#formElementsStruct.getType().addStructProperty(this.#groupElementStructName);
      }
      const groupElements: Struct = this.#formElementsStruct.get(this.#groupElementStructName);
      groupElements.getType().addStructProperty(newOptionText);

      //Reset the textField for the new GroupElement's name
      this.#addOptionVE.setValue("");

      FormUtils.reloadPreview();
    } else {
      MessageBoxWindow.getInstance().alert("Fehler", "Geben Sie den Namen des neuen Buttons in das Textfeld ein.");
    }
  }

  protected override initStruct(struct: Struct): void {
    super.initStruct(struct);
    this.#formElementsStruct = struct;
    if (!this.#formElementsStruct.get(this.#groupElementStructName)) {
      this.#formElementsStruct.getType().addStructProperty(this.#groupElementStructName);
    }
  }

  getAddOptionVE(): ValueExpression {
    if (!this.#addOptionVE) {
      this.#addOptionVE = ValueExpressionFactory.createFromValue("");
    }
    return this.#addOptionVE;
  }

  getGroupElementsVE(config: Config<FormEditorField>): ValueExpression {
    return ValueExpressionFactory.createFromFunction((): Array<any> => {
      const groupsVE = this.getPropertyVE(config);
      if (groupsVE.getValue()) {
        const groupElementStruct: Struct = groupsVE.getValue();
        return groupElementStruct.getType().getPropertyNames().map((name: string): GroupElementStructWrapper =>
          new GroupElementStructWrapper(groupsVE.extendBy(name), name, bind(this, this.onDefaultSelectionChanged)),
        );
      } else {
        return undefined;
      }
    });
  }

  /**
   * remove an option from the list
   * @param value
   */
  removeGroupElement(value: string): void {
    const groupElementsList: Struct = this.#formElementsStruct.get(this.#groupElementStructName);
    groupElementsList.getType().removeProperty(value);
    FormUtils.reloadPreview();
  }

  /**
   * updates an existing option
   * @param selectedOption existing option
   * @param newId new display value
   * @param newValue new option value
   * @param newChecked new checked state
   */
  updateGroupElement(selectedOption: GroupElementStructWrapper, newId: string, newValue: string, newChecked: boolean): void {

    //update sub properties
    selectedOption.getOptionValueVE().setValue(newValue);
    selectedOption.getOptionSelectedByDefaultVE().setValue(newChecked);

    //rename struct
    const groupElementsList: Struct = this.#formElementsStruct.get(this.#groupElementStructName);
    groupElementsList.getType().renameProperty(selectedOption.getId(), newId);

    FormUtils.reloadPreview();
  }

  /**
   * moves an existing option in the list of all available options
   * @param id option to move
   * @param moveDistance positions to move, 1 for up, -1 for down
   */
  moveGroupElement(id: string, moveDistance: number): void {
    const groupElementsList: Struct = this.#formElementsStruct.get(this.#groupElementStructName);
    const optionIds = groupElementsList.getType().getPropertyNames();

    const startingIndex: number = optionIds.indexOf(id);
    if (startingIndex == -1) {
      return;
    }

    let targetIndex: number = startingIndex + moveDistance;
    if (targetIndex >= (optionIds.length)) {
      targetIndex = 0;
    }
    if (targetIndex < 0) {
      targetIndex = optionIds.length - 1;
    }

    optionIds.splice(targetIndex, 0, optionIds.splice(startingIndex, 1)[0]);

    groupElementsList.getType().rearrangeProperties(optionIds);
    FormUtils.reloadPreview();
  }

  /**
   * unselect all other options if allowMultiDefaultSelection is false
   *
   * @param optionId
   * @param selected
   */
  onDefaultSelectionChanged(optionId: string, selected: boolean): void {
    if (!this.allowMultiDefaultSelection && selected) {

      const groupElementsList: Struct = this.#formElementsStruct.get(this.#groupElementStructName);
      const optionIds = groupElementsList.getType().getPropertyNames();

      optionIds.forEach((id: string): void => {
        const optionStruct: Struct = groupElementsList.get(id);
        optionStruct.set(GroupElementStructWrapper.PROP_CHECKED, id == optionId);
      });
    }
  }

  protected getAddOptionButtonDisabledVE(): ValueExpression {
    return ValueExpressionFactory.createFromFunction((): boolean => {
      const optionName: string = this.getAddOptionVE().getValue();
      return this.forceReadOnlyValueExpression.getValue() || optionName == null || !Format.trim(optionName).length;
    });
  }

}

export default AddOptionFieldBase;
