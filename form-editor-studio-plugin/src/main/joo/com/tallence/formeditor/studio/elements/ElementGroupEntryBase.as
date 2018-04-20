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

package com.tallence.formeditor.studio.elements {
import com.coremedia.cms.editor.sdk.editorContext;
import com.tallence.formeditor.studio.model.GroupElementStructWrapper;

import ext.panel.Panel;

public class ElementGroupEntryBase extends Panel {

  [Bindable]
  public var removeGroupElementFn:Function;

  [Bindable]
  public var option:GroupElementStructWrapper;

  public function ElementGroupEntryBase(config:ElementGroupEntryBase = null) {
    super(config);
  }

  internal function removeOption():void {
    this.removeGroupElementFn.call(NaN, this.option.getId());
  }

}
}
