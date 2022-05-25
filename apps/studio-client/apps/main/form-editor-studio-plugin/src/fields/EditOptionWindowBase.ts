/*
 * Copyright 2020 Tallence AG
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
import Window from "@jangaroo/ext-ts/window/Window";
import Config from "@jangaroo/runtime/Config";
import {AnyFunction} from "@jangaroo/runtime/types";
import GroupElementStructWrapper from "../model/GroupElementStructWrapper";
import EditOptionWindow from "./EditOptionWindow";
import FormUtils from "../FormUtils";

interface EditOptionWindowBaseConfig extends Config<Window>, Partial<Pick<EditOptionWindow,
        "option" | "onSaveCallback" | "onRemoveCallback">> {
}

class EditOptionWindowBase extends Window {
  declare Config: EditOptionWindowBaseConfig;

  option: GroupElementStructWrapper = null;

  onSaveCallback: AnyFunction = null;

  onRemoveCallback: AnyFunction = null;

  protected optionNameVE: ValueExpression = null;

  protected optionValueVE: ValueExpression = null;

  protected optionCheckedVE: ValueExpression = null;

  constructor(config: Config<EditOptionWindow> = null) {
    super(config);

    this.option = config.option;
    this.onSaveCallback = config.onSaveCallback;
    this.onRemoveCallback = config.onRemoveCallback;

    this.#initValues();
  }

  #initValues(): void {
    this.getOptionNameVE().setValue(this.option.getId());
    this.getOptionValueVE().setValue(this.option.getOptionValueVE().getValue());
    this.getOptionCheckedVE().setValue(this.option.getOptionSelectedByDefaultVE().getValue());
  }

  saveOption(): void {
    if (this.onSaveCallback) {
      this.onSaveCallback.call(NaN,
              this.getOptionNameVE().getValue(),
              this.getOptionValueVE().getValue(),
              this.getOptionCheckedVE().getValue(),
      );
      this.close();
    }
  }

  removeOption(): void {
    if (this.onRemoveCallback) {
      this.onRemoveCallback.call(NaN);
      this.close();
    }
  }

  protected getOptionNameVE(): ValueExpression {
    if (!this.optionNameVE) {
      this.optionNameVE = ValueExpressionFactory.createFromValue(null);
    }
    return this.optionNameVE;
  }

  protected getOptionValueVE(): ValueExpression {
    if (!this.optionValueVE) {
      this.optionValueVE = ValueExpressionFactory.createFromValue(null);
    }
    return this.optionValueVE;
  }

  protected getOptionCheckedVE(): ValueExpression {
    if (!this.optionCheckedVE) {
      this.optionCheckedVE = ValueExpressionFactory.createFromValue(false);
    }
    return this.optionCheckedVE;
  }

  protected getSaveButtonDisabledVE(): ValueExpression {
    return ValueExpressionFactory.createFromFunction((): boolean => {
      const displayName: string = this.getOptionNameVE().getValue();
      return !FormUtils.validateOptionValue(displayName);
    });
  }

}

export default EditOptionWindowBase;
