import BindPropertyPlugin from "@coremedia/studio-client.ext.ui-components/plugins/BindPropertyPlugin";
import BindDisablePlugin from "@coremedia/studio-client.main.editor-components/sdk/premular/fields/plugins/BindDisablePlugin";
import FieldContainer from "@jangaroo/ext-ts/form/FieldContainer";
import Checkbox from "@jangaroo/ext-ts/form/field/Checkbox";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import CheckboxFieldBase from "./CheckboxFieldBase";

interface CheckboxFieldConfig extends Config<CheckboxFieldBase> {
}

class CheckboxField extends CheckboxFieldBase {
  declare Config: CheckboxFieldConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.editor.fields.checkboxField";

  constructor(config: Config<CheckboxField> = null) {
    super((()=> ConfigUtils.apply(Config(CheckboxField, {

      items: [
        Config(FieldContainer, {
          fieldLabel: config.fieldLabel,
          items: [
            Config(Checkbox, {
              boxLabel: ConfigUtils.asString(config.boxLabel ? config.boxLabel : ""),
              plugins: [
                /* a checkbox cannot be set to undefined, so skipIfUndefined is set to true */
                Config(BindPropertyPlugin, {
                  bidirectional: true,
                  skipIfUndefined: true,
                  bindTo: this.getPropertyVE(config),
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

    }), config))());
  }
}

export default CheckboxField;
