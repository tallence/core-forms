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

import StudioPlugin from "@coremedia/studio-client.main.editor-components/configuration/StudioPlugin";
import IEditorContext from "@coremedia/studio-client.main.editor-components/sdk/IEditorContext";
import Config from "@jangaroo/runtime/Config";
import FormsStudioPlugin from "./FormsStudioPlugin";
import editorContext from "@coremedia/studio-client.main.editor-components/sdk/editorContext";
import Content from "@coremedia/studio-client.cap-rest-client/content/Content";
import Struct from "@coremedia/studio-client.cap-rest-client/struct/Struct";
import ContentInitializer from "@coremedia-blueprint/studio-client.main.blueprint-forms/util/ContentInitializer";
import FormElementsManager from "./helper/FormElementsManager";
import FormEditor_properties from "./bundles/FormEditor_properties";
import FormElementStructWrapper from "./model/FormElementStructWrapper";
import FormEditorForm from "./studioform/FormEditorForm";

interface FormsStudioPluginBaseConfig extends Config<StudioPlugin> {
}

class FormsStudioPluginBase extends StudioPlugin {
  declare Config: FormsStudioPluginBaseConfig;

  constructor(config: Config<FormsStudioPlugin> = null) {
    super(config);
  }

  override init(editorContext: IEditorContext): void {
    super.init(editorContext);
    FormsStudioPluginBase.applyInitializers();
  }

  static applyInitializers(): void {
    editorContext._.registerContentInitializer(FormsStudioPlugin.FORM_EDITOR_DOCTYPE, FormsStudioPluginBase.#initForm);
  }

  static #initForm(content: Content): void {
    ContentInitializer.initCMLinkable(content);

    content.set(FormEditorForm.PAGEABLE_ENABLED, 1);
    FormsStudioPluginBase.initInitialPage(content);
  }

  static initInitialPage(content: Content): void {
    FormsStudioPluginBase.initInitialElements(content);
    const formData: Struct = content.getProperties().get(FormsStudioPlugin.FORM_ELEMENTS_STRUCT_PROPERTY);
    const formElements: Struct = formData.get(FormElementStructWrapper.FORM_ELEMENTS_PROPERTY);
    formElements.getType().addStructProperty(
            FormElementsManager.generateRandomId().toString(),
            FormElementsManager.getPageInitialData(FormEditor_properties.FormEditor_pages_default_title));

  }

  static initInitialElements(content: Content): void {
    const formData: Struct = content.getProperties().get(FormsStudioPlugin.FORM_ELEMENTS_STRUCT_PROPERTY);
    if (formData.getType().getPropertyNames() && formData.getType().getPropertyNames().indexOf(FormElementStructWrapper.FORM_ELEMENTS_PROPERTY) != -1) {
      formData.getType().removeProperty(FormElementStructWrapper.FORM_ELEMENTS_PROPERTY);
    }
    formData.getType().addStructProperty(FormElementStructWrapper.FORM_ELEMENTS_PROPERTY);
  }

}

export default FormsStudioPluginBase;
