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

import Validators_properties
  from "@coremedia/studio-client.ext.errors-validation-components/validation/Validators_properties";
import com_coremedia_blueprint_base_queryeditor_QueryEditor_properties
  from "@coremedia/studio-client.main.bpbase-studio-dynamic-query-list/QueryEditor_properties";
import CopyResourceBundleProperties
  from "@coremedia/studio-client.main.editor-components/configuration/CopyResourceBundleProperties";
import AddTabbedDocumentFormsPlugin
  from "@coremedia/studio-client.main.editor-components/sdk/plugins/AddTabbedDocumentFormsPlugin";
import TabbedDocumentFormDispatcher
  from "@coremedia/studio-client.main.editor-components/sdk/premular/TabbedDocumentFormDispatcher";
import Config from "@jangaroo/runtime/Config";

import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import resourceManager from "@jangaroo/runtime/l10n/resourceManager";
import FormsStudioPluginBase from "./FormsStudioPluginBase";
import FormValidation_properties from "./bundles/FormValidation_properties";
import com_tallence_formeditor_studio_bundles_QueryEditor_properties from "./bundles/QueryEditor_properties";
import FormEditorForm from "./studioform/FormEditorForm";

interface FormsStudioPluginConfig extends Config<FormsStudioPluginBase> {
}

class FormsStudioPlugin extends FormsStudioPluginBase {
  declare Config: FormsStudioPluginConfig;

  static readonly FORM_EDITOR_DOCTYPE: string = "FormEditor";
  static readonly FORM_ELEMENTS_STRUCT_PROPERTY: string = "formData";
  static readonly PAGEABLE_ENABLED: string = "pageableFormEnabled";
  static readonly xtype: string = "com.tallence.formeditor.studio.config.formsStudioPlugin";

  constructor(config: Config<FormsStudioPlugin> = null) {
    super(ConfigUtils.apply(Config(FormsStudioPlugin, {

      rules: [

        Config(TabbedDocumentFormDispatcher, {
          plugins: [
            Config(AddTabbedDocumentFormsPlugin, {
              documentTabPanels: [
                Config(FormEditorForm, {itemId: FormsStudioPlugin.FORM_EDITOR_DOCTYPE}),
              ],
            }),
          ],
        }),

      ],

      configuration: [

        new CopyResourceBundleProperties({
          destination: resourceManager.getResourceBundle(null, com_coremedia_blueprint_base_queryeditor_QueryEditor_properties),
          source: resourceManager.getResourceBundle(null, com_tallence_formeditor_studio_bundles_QueryEditor_properties),
        }),
        new CopyResourceBundleProperties({
          destination: resourceManager.getResourceBundle(null, Validators_properties),
          source: resourceManager.getResourceBundle(null, FormValidation_properties),
        }),

      ],

    }), config));
  }
}

export default FormsStudioPlugin;
