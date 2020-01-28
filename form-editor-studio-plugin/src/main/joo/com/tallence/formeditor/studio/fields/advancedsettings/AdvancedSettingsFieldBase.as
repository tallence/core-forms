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

package com.tallence.formeditor.studio.fields.advancedsettings {
import com.coremedia.cap.struct.Struct;
import com.tallence.formeditor.studio.fields.*;

public class AdvancedSettingsFieldBase extends FormEditorField {

  public static const CUSTOM_ID:String = "customId";
  public static const COLUMN_WIDTH:String = "columnWidth";
  public static const VISIBILITY:String = "visibility";
  public static const VISIBILITY_ELEMENT_ID:String = "elementId";
  public static const VISIBILITY_ACTIVATED:String = "activated";
  public static const VISIBILITY_VALUE:String = "value";

  public function AdvancedSettingsFieldBase(config:TextFieldBase = null) {
    super(config);
  }

  /**
   * Initializes the required sub struct for the advanced settings tabs
   * {@link com.tallence.formeditor.studio.fields.advancedsettings.tabs.AdvancedSettingsTab}.
   * @param struct the parent struct of the form element
   */
  override protected function initStruct(struct:Struct):void {
    var visibility:Object = {};
    visibility[VISIBILITY_ACTIVATED] = false;
    visibility[VISIBILITY_ELEMENT_ID] = "";
    visibility[VISIBILITY_VALUE] = "";

    var advancedSettings:Object = {};
    advancedSettings[VISIBILITY] = visibility;
    advancedSettings[COLUMN_WIDTH] = 0;
    advancedSettings[CUSTOM_ID] = "";
    struct.getType().addStructProperty(propertyName, advancedSettings);
  }

}
}
