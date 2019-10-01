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

package com.tallence.formeditor.studio.components {
import com.coremedia.cms.editor.sdk.premular.CollapsiblePanel;
import com.coremedia.ui.mixins.IHighlightableMixin;
import com.coremedia.ui.mixins.IValidationStateMixin;
import com.coremedia.ui.mixins.ValidationState;

/**
 * This component extends the {@link CollapsiblePanel} and implements the {@link IValidationStateMixin} and
 * {@link IHighlightableMixin} interface to display a validation state.
 */
public class StateFulCollapsiblePanelBase extends CollapsiblePanel implements IValidationStateMixin, IHighlightableMixin {

  public function StateFulCollapsiblePanelBase(config:StateFulCollapsiblePanel = null) {
    super(config);
    this.initValidationStateMixin();
  }

  /** @inheritDoc */
  public native function initValidationStateMixin():void;

  /** @private */
  [Bindable(event="DUMMY")]
  public native function set validationState(validationState:ValidationState):void;

  /** @private */
  [Bindable(event="DUMMY")]
  public native function set validationMessage(validationMessage:String):void;

  /** @inheritDoc */
  [Bindable(event="DUMMY")]
  public native function get validationState():ValidationState;

  /** @inheritDoc */
  [Bindable(event="DUMMY")]
  public native function get validationMessage():String;

  /** @private */
  [Bindable(event="DUMMY")]
  public native function set highlighted(newHighlighted:Boolean):void;

  /** @inheritDoc */
  [Bindable(event="DUMMY")]
  public native function get highlighted():Boolean;
}
}
