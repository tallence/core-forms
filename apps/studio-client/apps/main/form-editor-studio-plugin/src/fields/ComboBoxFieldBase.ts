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

import Config from "@jangaroo/runtime/Config";
import FormEditorField from "./FormEditorField";
import TextFieldBase from "./TextFieldBase";
import ValueExpression from "@coremedia/studio-client.client-core/data/ValueExpression";

interface ComboBoxFieldBaseConfig extends Config<FormEditorField>, Partial<Pick<ComboBoxFieldBase,
        "defaultValue"
        >> {
}

class ComboBoxFieldBase extends FormEditorField {
  declare Config: ComboBoxFieldBaseConfig;

  #defaultValue: string = "";

  get defaultValue(): string {
    return this.#defaultValue;
  }

  set defaultValue(value: string) {
    this.#defaultValue = value;
  }

  constructor(config: Config<ComboBoxFieldBase> = null) {
    super(config);
  }

  protected override initWithDefault(ve: ValueExpression): void {
    if (this.defaultValue != undefined) {
      ve.setValue(this.defaultValue);
    }
  }

}

export default ComboBoxFieldBase;
