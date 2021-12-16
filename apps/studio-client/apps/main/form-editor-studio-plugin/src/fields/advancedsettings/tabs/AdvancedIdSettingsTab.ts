import StatefulTextField from "@coremedia/studio-client.ext.ui-components/components/StatefulTextField";
import BindPropertyPlugin from "@coremedia/studio-client.ext.ui-components/plugins/BindPropertyPlugin";
import BindDisablePlugin from "@coremedia/studio-client.main.editor-components/sdk/premular/fields/plugins/BindDisablePlugin";
import FieldContainer from "@jangaroo/ext-ts/form/FieldContainer";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import FormEditor_properties from "../../../bundles/FormEditor_properties";
import AdvancedSettingsFieldBase from "../AdvancedSettingsFieldBase";
import AdvancedSettingsTab from "./AdvancedSettingsTab";

interface AdvancedIdSettingsTabConfig extends Config<AdvancedSettingsTab> {
}

class AdvancedIdSettingsTab extends AdvancedSettingsTab {
  declare Config: AdvancedIdSettingsTabConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.editor.fields.advancedsettings.tabs.advancedIdSettingsTab";

  constructor(config: Config<AdvancedIdSettingsTab> = null) {
    super(ConfigUtils.apply(Config(AdvancedIdSettingsTab, {
      title: FormEditor_properties.FormEditor_advancedSettings_tabs_id_title,
      description: FormEditor_properties.FormEditor_advancedSettings_tabs_id_description,
      items: [
        Config(FieldContainer, {
          fieldLabel: FormEditor_properties.FormEditor_advancedSettings_tabs_id_fieldLabel,
          items: [
            Config(StatefulTextField, {
              emptyText: FormEditor_properties.FormEditor_advancedSettings_tabs_id_emptyText,
              width: "100%",
              plugins: [
                Config(BindPropertyPlugin, {
                  bidirectional: true,
                  bindTo: config.advancedSettingsVE.extendBy(AdvancedSettingsFieldBase.CUSTOM_ID),
                }),
                Config(BindDisablePlugin, {
                  bindTo: config.bindTo,
                  forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
                }),
              ],
            }),
          ],
        }),
      ],

    }), config));
  }
}

export default AdvancedIdSettingsTab;
