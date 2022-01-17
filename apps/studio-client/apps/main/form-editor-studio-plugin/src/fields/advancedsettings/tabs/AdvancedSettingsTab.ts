import ValueExpression from "@coremedia/studio-client.client-core/data/ValueExpression";
import DisplayFieldSkin from "@coremedia/studio-client.ext.ui-components/skins/DisplayFieldSkin";
import DisplayField from "@jangaroo/ext-ts/form/field/Display";
import Panel from "@jangaroo/ext-ts/panel/Panel";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";

interface AdvancedSettingsTabConfig extends Config<Panel>, Partial<Pick<AdvancedSettingsTab,
  "bindTo" |
  "forceReadOnlyValueExpression" |
  "advancedSettingsVE" |
  "description"
>> {
}

class AdvancedSettingsTab extends Panel {
  declare Config: AdvancedSettingsTabConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.editor.fields.advancedsettings.tabs.advancedSettingsTab";

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

  #advancedSettingsVE: ValueExpression = null;

  get advancedSettingsVE(): ValueExpression {
    return this.#advancedSettingsVE;
  }

  set advancedSettingsVE(value: ValueExpression) {
    this.#advancedSettingsVE = value;
  }

  #description: string = null;

  get description(): string {
    return this.#description;
  }

  set description(value: string) {
    this.#description = value;
  }

  constructor(config: Config<AdvancedSettingsTab> = null) {
    super(ConfigUtils.apply(Config(AdvancedSettingsTab, {

      dockedItems: [
        Config(DisplayField, {
          ui: ConfigUtils.asString(DisplayFieldSkin.ITALIC),
          margin: "10 0 10 0",
          dock: "top",
          value: config.description,
        }),
      ],

    }), config));
  }
}

export default AdvancedSettingsTab;
