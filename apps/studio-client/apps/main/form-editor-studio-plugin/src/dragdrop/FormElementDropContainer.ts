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
import BEMPlugin from "@coremedia/studio-client.ext.ui-components/plugins/BEMPlugin";
import Editor_properties from "@coremedia/studio-client.main.editor-components/Editor_properties";
import Container from "@jangaroo/ext-ts/container/Container";
import Label from "@jangaroo/ext-ts/form/Label";
import CenterLayout from "@jangaroo/ext-ts/layout/container/Center";
import HBoxLayout from "@jangaroo/ext-ts/layout/container/HBox";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import FormEditor_properties from "../bundles/FormEditor_properties";
import FormElementsManager from "../helper/FormElementsManager";
import FormElementDropContainerBase from "./FormElementDropContainerBase";
import CoreIcons_properties from "@coremedia/studio-client.core-icons/CoreIcons_properties";

interface FormElementDropContainerConfig extends Config<FormElementDropContainerBase>, Partial<Pick<FormElementDropContainer,
  "formElementsManager" |
  "formElementId" |
  "forceReadOnlyValueExpression"
>> {
}

class FormElementDropContainer extends FormElementDropContainerBase {
  declare Config: FormElementDropContainerConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.config.formElementDropContainer";

  #formElementsManager: FormElementsManager = null;

  get formElementsManager(): FormElementsManager {
    return this.#formElementsManager;
  }

  set formElementsManager(value: FormElementsManager) {
    this.#formElementsManager = value;
  }

  #formElementId: string = null;

  get formElementId(): string {
    return this.#formElementId;
  }

  set formElementId(value: string) {
    this.#formElementId = value;
  }

  #forceReadOnlyValueExpression: ValueExpression = null;

  get forceReadOnlyValueExpression(): ValueExpression {
    return this.#forceReadOnlyValueExpression;
  }

  set forceReadOnlyValueExpression(value: ValueExpression) {
    this.#forceReadOnlyValueExpression = value;
  }

  constructor(config: Config<FormElementDropContainer> = null) {
    super((()=> ConfigUtils.apply(Config(FormElementDropContainer, {

      plugins: [
        Config(BEMPlugin, {
          block: "form-element-drop-container",
          modifier: this.getModifiers(config),
        }),
      ],
      items: [
        Config(Container, {
          items: [
            Config(Container, {
              cls: CoreIcons_properties.link,
              width: 16,
              height: 16,
            }),
            Config(Label, { text: FormEditor_properties.FormEditor_text_add_element }),
          ],
          layout: Config(HBoxLayout),
        }),
      ],
      layout: Config(CenterLayout),

    }), config))());
  }
}

export default FormElementDropContainer;
