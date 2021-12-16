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

import editorContext from "@coremedia/studio-client.main.editor-components/sdk/editorContext";
import PreviewPanel from "@coremedia/studio-client.main.editor-components/sdk/preview/PreviewPanel";
import Panel from "@jangaroo/ext-ts/panel/Panel";
import { as, cast } from "@jangaroo/runtime";
import FormEditor_properties from "./bundles/FormEditor_properties";

class FormUtils {

  static reloadPreview(): void {
    const premular = cast(Panel, editorContext._.getWorkArea().getActiveTab());
    const previewPanel = as(premular.find("itemId", "previewPanel")[0], PreviewPanel);
    if (previewPanel) {
      previewPanel.reloadFrame();
    }
  }

  /**
   * Gets the condition title based on its propertyName.
   * @param key A condition's propertyName.
   * @return Condition title.
   */
  static getConditionTitle(key: string): string {
    //message property keys are separated with '_' by convention and not with '.' ->
    // If a key contains a '.' it will be replaced.
    let keyToUse = key.replace(".", "_");
    keyToUse = keyToUse.charAt(0).toLowerCase() + keyToUse.substring(1);
    return FormEditor_properties["FormEditor_label_element_" + keyToUse] || key;
  }

}

export default FormUtils;
