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

package com.tallence.formeditor.studio.fields {

import com.coremedia.ui.data.ValueExpression;
import com.coremedia.ui.data.ValueExpressionFactory;
import com.tallence.formeditor.studio.model.GroupElementStructWrapper;

import ext.util.Format;

import ext.window.Window;

public class EditOptionWindowBase extends Window {

  protected var option:GroupElementStructWrapper;

  protected var optionNameVE:ValueExpression;
  protected var optionValueVE:ValueExpression;
  protected var optionCheckedVE:ValueExpression;

  protected var onSaveCallback:Function;
  protected var onRemoveCallback:Function;

  public function EditOptionWindowBase(config:EditOptionWindow = null) {
    super(config);

    this.option = config.option;
    this.onSaveCallback = config.onSaveCallback;
    this.onRemoveCallback = config.onRemoveCallback;

    initValues();
  }

  private function initValues():void {
    getOptionNameVE().setValue(option.getId());
    getOptionValueVE().setValue(option.getOptionValueVE().getValue());
    getOptionCheckedVE().setValue(option.getOptionSelectedByDefaultVE().getValue());
  }

  public function saveOption():void {
    if (this.onSaveCallback) {
      this.onSaveCallback.call(NaN,
              getOptionNameVE().getValue(),
              getOptionValueVE().getValue(),
              getOptionCheckedVE().getValue()
      );
      close();
    }
  }

  public function removeOption():void {
    if (this.onRemoveCallback) {
      this.onRemoveCallback.call(NaN);
      close();
    }
  }

  protected function getOptionNameVE():ValueExpression {
    if (!optionNameVE) {
      optionNameVE = ValueExpressionFactory.createFromValue(null);
    }
    return optionNameVE;
  }

  protected function getOptionValueVE():ValueExpression {
    if (!optionValueVE) {
      optionValueVE = ValueExpressionFactory.createFromValue(null);
    }
    return optionValueVE;
  }

  protected function getOptionCheckedVE():ValueExpression {
    if (!optionCheckedVE) {
      optionCheckedVE = ValueExpressionFactory.createFromValue(false);
    }
    return optionCheckedVE;
  }

  protected function getSaveButtonDisabledVE():ValueExpression {
    return ValueExpressionFactory.createFromFunction(function ():Boolean {
      var displayName:String = getOptionNameVE().getValue();
      return displayName == null || !Format.trim(displayName).length;
    })
  }

}
}
