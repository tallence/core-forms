import StatefulCheckbox from "@coremedia/studio-client.ext.ui-components/components/StatefulCheckbox";
import StatefulNumberField from "@coremedia/studio-client.ext.ui-components/components/StatefulNumberField";
import BindPropertyPlugin from "@coremedia/studio-client.ext.ui-components/plugins/BindPropertyPlugin";
import BindDisablePlugin from "@coremedia/studio-client.main.editor-components/sdk/premular/fields/plugins/BindDisablePlugin";
import FieldContainer from "@jangaroo/ext-ts/form/FieldContainer";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import FormEditor_properties from "../../../bundles/FormEditor_properties";
import AdvancedSettingsFieldBase from "../AdvancedSettingsFieldBase";
import AdvancedSettingsTab from "./AdvancedSettingsTab";

interface AdvancedLayoutSettingsTabConfig extends Config<AdvancedSettingsTab> {
}

class AdvancedLayoutSettingsTab extends AdvancedSettingsTab {
  declare Config: AdvancedLayoutSettingsTabConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.editor.fields.advancedsettings.tabs.advancedLayoutSettingsTab";

  constructor(config: Config<AdvancedLayoutSettingsTab> = null) {
    super(ConfigUtils.apply(Config(AdvancedLayoutSettingsTab, {
      title: FormEditor_properties.FormEditor_advancedSettings_tabs_layout_title,
      description: FormEditor_properties.FormEditor_advancedSettings_tabs_layout_description,
      items: [
        Config(FieldContainer, {
          fieldLabel: FormEditor_properties.FormEditor_advancedSettings_tabs_layout_columnWidth_fieldLabel,
          items: [
            Config(StatefulNumberField, {
              emptyText: FormEditor_properties.FormEditor_advancedSettings_tabs_layout_columnWidth_emptyText,
              width: "100%",
              plugins: [
                Config(BindPropertyPlugin, {
                  bidirectional: true,
                  bindTo: config.advancedSettingsVE.extendBy(AdvancedSettingsFieldBase.COLUMN_WIDTH),
                }),
                Config(BindDisablePlugin, {
                  bindTo: config.bindTo,
                  forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
                }),
              ],
            }),
          ],
        }),
        Config(FieldContainer, {
          fieldLabel: FormEditor_properties.FormEditor_advancedSettings_tabs_layout_breakAfter_fieldLabel,
          items: [
            Config(StatefulCheckbox, {
              plugins: [
                Config(BindPropertyPlugin, {
                  bidirectional: true,
                  skipIfUndefined: true,
                  bindTo: config.advancedSettingsVE.extendBy(AdvancedSettingsFieldBase.BREAK_AFTER_ELEMENT),
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

export default AdvancedLayoutSettingsTab;
