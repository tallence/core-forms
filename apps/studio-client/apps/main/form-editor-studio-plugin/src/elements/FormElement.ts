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

import ValueExpression from "@coremedia/studio-client.client-core/data/ValueExpression";
import FormElementStructWrapper from "../model/FormElementStructWrapper";

abstract class FormElement {

  /**
   * Returns the type of the form element.
   */
  abstract getFormElementType(): string;

  /**
   * Returns the icon class of the form element. Each applicable element can be identified by an icon in the form
   * editor.
   */
  abstract getFormElementIconCls(): string;

  /**
   * Returns the group name of the form element. Each applicable element is assigned to a group so that a graphical
   * division can be made in the editor.
   */
  abstract getFormElementGroup(): string;

  /**
   * Updates the struct wrapper. Since the editors of the form elements are reused, it must be possible to update
   * the struct wrapper.
   */
  abstract updateFormElementStructWrapper(wrapper: FormElementStructWrapper): void;

  /**
   * Returns the struct wrapper representing the form element.
   */
  abstract getFormElementStructWrapper(): FormElementStructWrapper;

  /**
   * Returns a value expression evaluating to the struct of the form element.
   */
  abstract getFormElementStructVE(): ValueExpression;

}

export default FormElement;
