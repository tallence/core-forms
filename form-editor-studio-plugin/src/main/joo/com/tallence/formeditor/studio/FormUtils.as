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

package com.tallence.formeditor.studio {

import com.coremedia.cms.editor.sdk.editorContext;
import com.coremedia.cms.editor.sdk.preview.PreviewPanel;

import ext.panel.Panel;

import mx.resources.ResourceManager;

public class FormUtils {

  public static function reloadPreview():void {
    var premular:Panel = Panel(editorContext.getWorkArea().getActiveTab());
    var previewPanel:PreviewPanel = premular.find('itemId', 'previewPanel')[0] as PreviewPanel;
    if (previewPanel) {
      previewPanel.reloadFrame();
    }
  }

  /**
   * Gets the condition title based on its propertyName.
   * @param key A condition's propertyName.
   * @return Condition title.
   */
  public static function getConditionTitle(key:String):String {

    var keyToUse:String = key.replace('\.', '_');
    keyToUse = keyToUse.charAt(0).toLowerCase() + keyToUse.substring(1);
    return ResourceManager.getInstance().getString("com.tallence.formeditor.studio.bundles.FormEditor",
            'FormEditor_label_element_' + keyToUse) || key;
  }

}
}
