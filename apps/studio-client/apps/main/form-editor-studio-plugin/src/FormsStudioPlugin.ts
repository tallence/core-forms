import Validators_properties from "@coremedia/studio-client.ext.errors-validation-components/validation/Validators_properties";
import com_coremedia_blueprint_base_queryeditor_QueryEditor_properties from "@coremedia/studio-client.main.bpbase-studio-dynamic-query-list/QueryEditor_properties";
import CopyResourceBundleProperties from "@coremedia/studio-client.main.editor-components/configuration/CopyResourceBundleProperties";
import AddTabbedDocumentFormsPlugin from "@coremedia/studio-client.main.editor-components/sdk/plugins/AddTabbedDocumentFormsPlugin";
import TabbedDocumentFormDispatcher from "@coremedia/studio-client.main.editor-components/sdk/premular/TabbedDocumentFormDispatcher";
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

  static readonly xtype: string = "com.tallence.formeditor.studio.config.formsStudioPlugin";

  constructor(config: Config<FormsStudioPlugin> = null) {
    super(ConfigUtils.apply(Config(FormsStudioPlugin, {

      rules: [

        Config(TabbedDocumentFormDispatcher, {
          plugins: [
            Config(AddTabbedDocumentFormsPlugin, {
              documentTabPanels: [
                Config(FormEditorForm, { itemId: "FormEditor" }),
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
