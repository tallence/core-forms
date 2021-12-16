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
import Component from "@jangaroo/ext-ts/Component";
import ComponentManager from "@jangaroo/ext-ts/ComponentManager";
import Container from "@jangaroo/ext-ts/container/Container";
import Panel from "@jangaroo/ext-ts/panel/Panel";
import { as, asConfig } from "@jangaroo/runtime";
import Config from "@jangaroo/runtime/Config";
import ApplicableElements from "./ApplicableElements";
import FormEditor_properties from "./bundles/FormEditor_properties";
import FormElementDroppable from "./dragdrop/FormElementDroppable";
import FormElement from "./elements/FormElement";

interface ApplicableElementsBaseConfig extends Config<Container> {
}

class ApplicableElementsBase extends Container {
  declare Config: ApplicableElementsBaseConfig;

  protected static readonly APPLICABLE_ELEMENTS_CONTAINER_ID: string = "groupsCt";

  #groupedFormElements: any = null;

  #groupsCt: Container = null;

  #dragActiveVE: ValueExpression = null;

  #readOnlyVE: ValueExpression = null;

  constructor(config: Config<ApplicableElements> = null) {
    super(config);
    this.#groupedFormElements = ApplicableElementsBase.#groupFormElements(config.formElements);
    this.#dragActiveVE = config.dragActiveVE;
    this.#readOnlyVE = config.readOnlyVE;
  }

  protected override afterRender(): void {
    super.afterRender();
    this.#renderGroupedConditions();
  }

  #renderGroupedConditions(): void {
    for (const group in this.#groupedFormElements) {
      const groupPanel: Panel = new CollapsiblePanel(Config(CollapsiblePanel, {
        title: this.#getGroupTitle(group),
        margin: "10 0 0 0",
      }));

      for (let i: number = 0; i < this.#groupedFormElements[group].length; i++) {
        const formElement: FormElement = this.#groupedFormElements[group][i];
        groupPanel.add(
          new FormElementDroppable(Config(FormElementDroppable, {
            width: 200,
            formElementType: formElement.getFormElementType(),
            formElementIconCls: formElement.getFormElementIconCls(),
            dragActiveVE: this.#dragActiveVE,
            readOnlyVE: this.#readOnlyVE,
          })),
        );
      }
      this.#getGroupsCt().add(groupPanel);
    }

  }

  #getGroupsCt(): Container {
    if (!this.#groupsCt) {
      this.#groupsCt = as(this.queryBy(ApplicableElementsBase.#groupContainerFilter)[0], Container);
    }
    return this.#groupsCt;
  }

  static #groupContainerFilter(component: Component): boolean {
    return asConfig(component).itemId === ApplicableElementsBase.APPLICABLE_ELEMENTS_CONTAINER_ID;
  }

  static #groupFormElements(formElements: Array<any>): any {
    let formElement: FormElement;
    let group: string;
    const groupedElements: Record<string, any> = {};

    for (let i: number = 0; i < formElements.length; i++) {
      formElement = as(ComponentManager.create(formElements[i]), FormElement);
      group = formElement.getFormElementGroup();
      if (!groupedElements[group]) {
        groupedElements[group] = [];
      }
      groupedElements[group].push(formElement);
    }

    return groupedElements;
  }

  #getGroupTitle(groupName: string): string {
    return FormEditor_properties["FormEditor_title_group_" + groupName] || groupName;
  }

}

export default ApplicableElementsBase;
