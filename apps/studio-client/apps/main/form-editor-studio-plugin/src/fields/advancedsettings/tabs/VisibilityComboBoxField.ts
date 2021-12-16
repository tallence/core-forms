import ValueExpression from "@coremedia/studio-client.client-core/data/ValueExpression";
import StatefulComboBox from "@coremedia/studio-client.ext.ui-components/components/StatefulComboBox";
import BindListPlugin from "@coremedia/studio-client.ext.ui-components/plugins/BindListPlugin";
import BindPropertyPlugin from "@coremedia/studio-client.ext.ui-components/plugins/BindPropertyPlugin";
import DataField from "@coremedia/studio-client.ext.ui-components/store/DataField";
import BindReadOnlyPlugin from "@coremedia/studio-client.main.editor-components/sdk/premular/fields/plugins/BindReadOnlyPlugin";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";

interface VisibilityComboBoxFieldConfig extends Config<StatefulComboBox>, Partial<Pick<VisibilityComboBoxField,
  "bindTo" |
  "forceReadOnlyValueExpression" |
  "elementsVE" |
  "selectedVE"
>> {
}

class VisibilityComboBoxField extends StatefulComboBox {
  declare Config: VisibilityComboBoxFieldConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.editor.fields.advancedsettings.tabs.visibilityComboBoxField";

  static readonly FIELD_ID: string = "id";

  static readonly FIELD_VALUE: string = "value";

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

  #elementsVE: ValueExpression = null;

  get elementsVE(): ValueExpression {
    return this.#elementsVE;
  }

  set elementsVE(value: ValueExpression) {
    this.#elementsVE = value;
  }

  #selectedVE: ValueExpression = null;

  get selectedVE(): ValueExpression {
    return this.#selectedVE;
  }

  set selectedVE(value: ValueExpression) {
    this.#selectedVE = value;
  }

  /**
   * Reusable combo box component used within the advanced visibility settings tab.
   */
  constructor(config: Config<VisibilityComboBoxField> = null) {
    super(ConfigUtils.apply(Config(VisibilityComboBoxField, {
      editable: false,
      width: "100%",
      displayField: VisibilityComboBoxField.FIELD_VALUE,
      valueField: VisibilityComboBoxField.FIELD_ID,
      ...ConfigUtils.append({
        plugins: [
          Config(BindListPlugin, {
            bindTo: config.elementsVE,
            fields: [
              Config(DataField, { name: VisibilityComboBoxField.FIELD_ID }),
              Config(DataField, { name: VisibilityComboBoxField.FIELD_VALUE }),
            ],
          }),
          Config(BindPropertyPlugin, {
            bindTo: config.selectedVE,
            skipIfUndefined: true,
            bidirectional: true,
            componentEvent: "select",
          }),
          Config(BindReadOnlyPlugin, {
            bindTo: config.bindTo,
            forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
          }),
        ],
      }),

    }), config));
  }
}

export default VisibilityComboBoxField;
