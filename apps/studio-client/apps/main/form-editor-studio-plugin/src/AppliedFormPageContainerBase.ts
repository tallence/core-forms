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
import PropertyEditorUtil from "@coremedia/studio-client.main.editor-components/sdk/util/PropertyEditorUtil";
import Container from "@jangaroo/ext-ts/container/Container";
import Config from "@jangaroo/runtime/Config";
import FormElementsManager from "./helper/FormElementsManager";
import FormElementStructWrapper from "./model/FormElementStructWrapper";
import PageElementEditor from "./elements/PageElementEditor";
import createComponentSelector from "@coremedia/studio-client.ext.ui-components/util/createComponentSelector";

interface AppliedFormPageContainerBaseConfig extends Config<Container>, Partial<Pick<AppliedFormPageContainerBase,
        "bindTo" |
        "forceReadOnlyValueExpression" |
        "formElement" |
        "formElementsManager">> {
}

class AppliedFormPageContainerBase extends Container {
  declare Config: AppliedFormPageContainerBaseConfig;

  protected static readonly FORM_ELEMENT_PANEL: string = "form-element-collapsible-panel";
  protected static readonly FORM_ELEMENT_HEADER: string = "form-element-header";
  protected static readonly FORM_PAGE_EDITOR: string = "pageEditor";

  #bindTo: ValueExpression = null;
  #forceReadOnlyValueExpression: ValueExpression = null;
  #formElement: FormElementStructWrapper = null;
  #formElementsManager: FormElementsManager = null;
  #readOnlyVE: ValueExpression = null;

  set bindTo(value: ValueExpression) {
    this.#bindTo = value;
  }

  set forceReadOnlyValueExpression(value: ValueExpression) {
    this.#forceReadOnlyValueExpression = value;
  }

  set formElement(value: FormElementStructWrapper) {
    this.#formElement = value;
  }

  set formElementsManager(value: FormElementsManager) {
    this.#formElementsManager = value;
  }

  constructor(config: Config<AppliedFormPageContainerBase> = null) {
    super(config);
    this.formElement = config.formElement;
    this.formElementsManager = config.formElementsManager;
    // Create a value expression to bind the disabled state of the drag source. It is necessary to use the two
    // value expressions 'bindTo' and 'forceReadOnlyValueExpression' to create the read only value expression. If a
    // content is checked out by another user, the read only value is not stored in the forceReadOnlyValueExpression.
    this.#readOnlyVE = PropertyEditorUtil.createReadOnlyValueExpression(config.bindTo, config.forceReadOnlyValueExpression);
  }

  protected override afterRender(): void {
    super.afterRender();
    const formElementEditor = this.query(createComponentSelector().itemId(AppliedFormPageContainerBase.FORM_PAGE_EDITOR).build())[0] as PageElementEditor;
    if (this.formElement != formElementEditor.getFormElementStructWrapper()) {
      formElementEditor.updateFormElementStructWrapper(this.formElement);
    }
  }

}

export default AppliedFormPageContainerBase;
