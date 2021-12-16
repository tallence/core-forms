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
import reusableComponentsService from "@coremedia/studio-client.ext.ui-components/util/reusableComponentsService";
import DocumentForm from "@coremedia/studio-client.main.editor-components/sdk/premular/DocumentForm";
import ComponentManager from "@jangaroo/ext-ts/ComponentManager";
import { cast } from "@jangaroo/runtime";
import Config from "@jangaroo/runtime/Config";
import FormEditorDocumentForm from "./FormEditorDocumentForm";
import AbstractFormElement from "./elements/AbstractFormElement";
import FormElementsManager from "./helper/FormElementsManager";

interface FormEditorDocumentFormBaseConfig extends Config<DocumentForm> {
}

class FormEditorDocumentFormBase extends DocumentForm {
  declare Config: FormEditorDocumentFormBaseConfig;

  #formElementsManager: FormElementsManager = null;

  constructor(config: Config<FormEditorDocumentForm> = null) {
    super(config);
  }

  /**
   * Each editor for a form element is reused. In order to use the
   * {@link com.coremedia.ui.util.IReusableComponentsService}, the component must first be created by the
   * {@link ext.ComponentManager}. Then the form editor component is registered and can be used afterwards.
   */
  protected initReusableComponents(formElements: Array<any>): void {
    for (let i = 0; i < formElements.length; i++) {
      const formElement = cast(AbstractFormElement, ComponentManager.create(formElements[i]));

      const key = formElement.getFormElementType();
      if (!reusableComponentsService.isReusabilityEnabled(key)) {
        reusableComponentsService.setReusabilityLimit(key, 1);
        reusableComponentsService.registerComponentForReuse(key, formElement);
      }
    }
  }

  protected getFormElementsManager(bindTo: ValueExpression,
    forceReadOnlyValueExpression: ValueExpression,
    propertyName: string): FormElementsManager {
    if (!this.#formElementsManager) {
      this.#formElementsManager = new FormElementsManager(bindTo, forceReadOnlyValueExpression, propertyName);
    }
    return this.#formElementsManager;
  }

}

export default FormEditorDocumentFormBase;
