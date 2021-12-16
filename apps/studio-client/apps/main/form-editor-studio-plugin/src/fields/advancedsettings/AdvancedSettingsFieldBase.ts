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
import Config from "@jangaroo/runtime/Config";
import FormEditorField from "../FormEditorField";
import TextFieldBase from "../TextFieldBase";

interface AdvancedSettingsFieldBaseConfig extends Config<FormEditorField> {
}

class AdvancedSettingsFieldBase extends FormEditorField {
  declare Config: AdvancedSettingsFieldBaseConfig;

  static readonly CUSTOM_ID: string = "customId";

  static readonly COLUMN_WIDTH: string = "columnWidth";

  static readonly BREAK_AFTER_ELEMENT: string = "breakAfterElement";

  static readonly VISIBILITY: string = "visibility";

  static readonly VISIBILITY_ELEMENT_ID: string = "elementId";

  static readonly VISIBILITY_ACTIVATED: string = "activated";

  static readonly VISIBILITY_VALUE: string = "value";

  constructor(config: Config<TextFieldBase> = null) {
    super(config);
  }

  /**
   * Initializes the required sub struct for the advanced settings tabs
   * {@link com.tallence.formeditor.studio.fields.advancedsettings.tabs.AdvancedSettingsTab}.
   * @param struct the parent struct of the form element
   */
  protected override initStruct(struct: Struct): void {
    super.initStruct(struct);
    const visibility: Record<string, any> = {};
    visibility[AdvancedSettingsFieldBase.VISIBILITY_ACTIVATED] = false;
    visibility[AdvancedSettingsFieldBase.VISIBILITY_ELEMENT_ID] = "";
    visibility[AdvancedSettingsFieldBase.VISIBILITY_VALUE] = "";

    const advancedSettings: Record<string, any> = {};
    advancedSettings[AdvancedSettingsFieldBase.VISIBILITY] = visibility;
    advancedSettings[AdvancedSettingsFieldBase.COLUMN_WIDTH] = 0;
    advancedSettings[AdvancedSettingsFieldBase.BREAK_AFTER_ELEMENT] = false;
    advancedSettings[AdvancedSettingsFieldBase.CUSTOM_ID] = "";
    struct.getType().addStructProperty(this.propertyName, advancedSettings);
  }

}

export default AdvancedSettingsFieldBase;
