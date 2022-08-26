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
import CollapsiblePanel from "@coremedia/studio-client.ext.ui-components/components/panel/CollapsiblePanel";
import reusableComponentsService from "@coremedia/studio-client.ext.ui-components/util/reusableComponentsService";
import PropertyEditorUtil from "@coremedia/studio-client.main.editor-components/sdk/util/PropertyEditorUtil";
import Component from "@jangaroo/ext-ts/Component";
import Container from "@jangaroo/ext-ts/container/Container";
import {as} from "@jangaroo/runtime";
import Config from "@jangaroo/runtime/Config";
import FormElement from "./elements/FormElement";
import FormElementsManager from "./helper/FormElementsManager";
import FormElementStructWrapper from "./model/FormElementStructWrapper";

interface AppliedFormPageContainerBaseConfig extends Config<Container>, Partial<Pick<AppliedFormPageContainerBase,
        "bindTo" |
        "forceReadOnlyValueExpression" |
        "formElement" |
        "formElementsManager">> {
}

/**
 * TODO: cleanup duplicated code from: AppliedFormElementsContainerBaseConfig
 */
class AppliedFormPageContainerBase extends Container {
  declare Config: AppliedFormPageContainerBaseConfig;

  protected static readonly FORM_ELEMENT_PANEL: string = "form-element-collapsible-panel";

  protected static readonly FORM_ELEMENT_HEADER: string = "form-element-header";

  #bindTo: ValueExpression = null;

  get bindTo(): ValueExpression {
    return this.#bindTo;
  }

  set bindTo(value: ValueExpression) {
    this.#bindTo = value;
  }

  #forceReadOnlyValueExpression: ValueExpression = null;

  get forceReadOnlyValueExpression(): ValueExpression {
    return this.#forceReadOnlyValueExpression;
  }

  set forceReadOnlyValueExpression(value: ValueExpression) {
    this.#forceReadOnlyValueExpression = value;
  }

  #formElement: FormElementStructWrapper = null;

  get formElement(): FormElementStructWrapper {
    return this.#formElement;
  }

  set formElement(value: FormElementStructWrapper) {
    this.#formElement = value;
  }

  #formElementsManager: FormElementsManager = null;

  get formElementsManager(): FormElementsManager {
    return this.#formElementsManager;
  }

  set formElementsManager(value: FormElementsManager) {
    this.#formElementsManager = value;
  }


  #readOnlyVE: ValueExpression = null;

  #panel: CollapsiblePanel = null;

  constructor(config: Config<AppliedFormPageContainerBase> = null) {
    super(config);
    this.formElement = config.formElement;
    this.formElementsManager = config.formElementsManager;
    // Create a value expression to bind the disabled state of the drag source. It is necessary to use the two
    // value expressions 'bindTo' and 'forceReadOnlyValueExpression' to create the read only value expression. If a
    // content is checked out by another user, the read only value is not stored in the forceReadOnlyValueExpression.
    this.#readOnlyVE = PropertyEditorUtil.createReadOnlyValueExpression(config.bindTo, config.forceReadOnlyValueExpression);
  }

  getFormElementsVE(): ValueExpression {
    return this.formElementsManager.getFormElementsVE();
  }

  protected override afterRender(): void {
    super.afterRender();
    const panel = as(this.queryById(AppliedFormPageContainerBase.FORM_ELEMENT_PANEL), CollapsiblePanel);

    const formElementEditor = as(reusableComponentsService.requestComponentForReuse(this.formElement.getType()), FormElement);
    if (this.formElement != formElementEditor.getFormElementStructWrapper()) {
      formElementEditor.updateFormElementStructWrapper(this.formElement);
      const component = as(formElementEditor, Component);
      if (component.isInstance) {
        panel.add(component);
      }
    }

    this.#panel = panel;
  }

  override destroy(...params): void {
    this.#removeReusableFormElement();
    super.destroy(params);
  }

  iconClassTransformer(elementType: string): string {
    const formElementEditor = as(reusableComponentsService.requestComponentForReuse(elementType), FormElement);
    return formElementEditor.getFormElementIconCls() || "";
  }

  removeElementHandler(): void {
    this.#removeReusableFormElement();
    this.formElementsManager.removeFormElement(this.formElement.getId());
  }

  /**
   * Before the applied form elements container is destroyed, the reusable form element must be removed from the
   * container. This means that the component can still be reused and is not destroyed itself.
   */
  #removeReusableFormElement(): void {
    reusableComponentsService.removeReusableComponentCleanly(reusableComponentsService.requestComponentForReuse(this.formElement.getType()));
  }
}

export default AppliedFormPageContainerBase;
