import StatefulTextField from "@coremedia/studio-client.ext.ui-components/components/StatefulTextField";
import BindPropertyPlugin from "@coremedia/studio-client.ext.ui-components/plugins/BindPropertyPlugin";
import BindDisablePlugin from "@coremedia/studio-client.main.editor-components/sdk/premular/fields/plugins/BindDisablePlugin";
import FieldContainer from "@jangaroo/ext-ts/form/FieldContainer";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import FormEditor_properties from "../bundles/FormEditor_properties";
import ShowFormIssuesPlugin from "../plugins/ShowFormIssuesPlugin";
import TextFieldBase from "./TextFieldBase";

interface TextFieldConfig extends Config<TextFieldBase>, Partial<Pick<TextField,
  "emptyText"
>> {
}

class TextField extends TextFieldBase {
  declare Config: TextFieldConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.editor.fields.textField";

  #emptyText: string = null;

  get emptyText(): string {
    return this.#emptyText;
  }

  set emptyText(value: string) {
    this.#emptyText = value;
  }

  constructor(config: Config<TextField> = null) {
    super((()=> ConfigUtils.apply(Config(TextField, {

      items: [
        Config(FieldContainer, {
          fieldLabel: config.fieldLabel,
          items: [
            Config(StatefulTextField, {
              emptyText: ConfigUtils.asString(config.emptyText ? config.emptyText : FormEditor_properties.FormEditor_element_textField_emptyText),
              width: "100%",
              plugins: [
                Config(ShowFormIssuesPlugin, {
                  issuesVE: config.formIssuesVE,
                  propertyName: config.propertyName,
                  propertyPathVE: config.propertyPathVE,
                }),
                Config(BindPropertyPlugin, {
                  bidirectional: true,
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

export default TextField;
