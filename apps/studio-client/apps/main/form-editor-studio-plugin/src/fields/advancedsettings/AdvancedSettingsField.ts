import CollapsiblePanel from "@coremedia/studio-client.main.editor-components/sdk/premular/CollapsiblePanel";
import TabPanel from "@jangaroo/ext-ts/tab/Panel";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import FormEditor_properties from "../../bundles/FormEditor_properties";
import AdvancedSettingsFieldBase from "./AdvancedSettingsFieldBase";
import AdvancedIdSettingsTab from "./tabs/AdvancedIdSettingsTab";
import AdvancedLayoutSettingsTab from "./tabs/AdvancedLayoutSettingsTab";
import AdvancedSettingsTab from "./tabs/AdvancedSettingsTab";
import AdvancedVisibilitySettingsTab from "./tabs/AdvancedVisibilitySettingsTab";

interface AdvancedSettingsFieldConfig extends Config<AdvancedSettingsFieldBase> {
}

class AdvancedSettingsField extends AdvancedSettingsFieldBase {
  declare Config: AdvancedSettingsFieldConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.editor.fields.advancedsettings.advancedSettingsField";

  constructor(config: Config<AdvancedSettingsField> = null) {
    super((()=> ConfigUtils.apply(Config(AdvancedSettingsField, {

      items: [
        Config(CollapsiblePanel, {
          title: FormEditor_properties.FormEditor_advancedSettings_title,
          collapsed: true,
          cls: "form-field-advanced-settings",
          items: [
            /* #b3b1b1 */
            Config(TabPanel, {
              cls: "advanced-settings-panel",
              bodyPadding: 20,
              items: [
                Config(AdvancedVisibilitySettingsTab),
                Config(AdvancedIdSettingsTab),
                Config(AdvancedLayoutSettingsTab),
              ],
              defaultType: AdvancedSettingsTab.xtype,
              defaults: Config<AdvancedSettingsTab>({
                bindTo: config.bindTo,
                forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
                advancedSettingsVE: this.getPropertyVE(config),
              }),
            }),
          ],
        }),
      ],

    }), config))());
  }
}

export default AdvancedSettingsField;
