import StatefulNumberField from "@coremedia/studio-client.ext.ui-components/components/StatefulNumberField";
import BindPropertyPlugin from "@coremedia/studio-client.ext.ui-components/plugins/BindPropertyPlugin";
import BindDisablePlugin from "@coremedia/studio-client.main.editor-components/sdk/premular/fields/plugins/BindDisablePlugin";
import FieldContainer from "@jangaroo/ext-ts/form/FieldContainer";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import FormEditor_properties from "../bundles/FormEditor_properties";
import ShowFormIssuesPlugin from "../plugins/ShowFormIssuesPlugin";
import NumberFieldBase from "./NumberFieldBase";

interface NumberFieldConfig extends Config<NumberFieldBase>, Partial<Pick<NumberField,
  "emptyText" |
  "minValue" |
  "maxValue"
>> {
}

class NumberField extends NumberFieldBase {
  declare Config: NumberFieldConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.editor.fields.numberField";

  #emptyText: string = null;

  get emptyText(): string {
    return this.#emptyText;
  }

  set emptyText(value: string) {
    this.#emptyText = value;
  }

  #minValue: number = NaN;

  get minValue(): number {
    return this.#minValue;
  }

  set minValue(value: number) {
    this.#minValue = value;
  }

  #maxValue: number = NaN;

  get maxValue(): number {
    return this.#maxValue;
  }

  set maxValue(value: number) {
    this.#maxValue = value;
  }

  constructor(config: Config<NumberField> = null) {
    super((()=> ConfigUtils.apply(Config(NumberField, {

      items: [
        Config(FieldContainer, {
          fieldLabel: config.fieldLabel,
          items: [
            /*
          - optional parameters for min and max added.
          - cannot use the constant Number.MIN_VALUE as default for minValue, this will cause an error in Studio.
            according to the documentation of Number.MIN_VALUE: "The smallest representable number overall is actually <code>-Number.MAX_VALUE</code>."
            that's why we are using -Number.MAX_VALUE as default here.
         */
            Config(StatefulNumberField, {
              emptyText: ConfigUtils.asString(config.emptyText ? config.emptyText : FormEditor_properties.FormEditor_element_numberField_emptyText),
              minValue: config.minValue ? config.minValue : -Number.MAX_VALUE,
              maxValue: config.maxValue ? config.maxValue : Number.MAX_VALUE,
              width: "100%",
              plugins: [
                Config(BindPropertyPlugin, {
                  bidirectional: true,
                  bindTo: this.getPropertyVE(config),
                }),
                Config(BindDisablePlugin, {
                  bindTo: config.bindTo,
                  forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
                }),
                Config(ShowFormIssuesPlugin, {
                  issuesVE: config.formIssuesVE,
                  propertyName: config.propertyName,
                  propertyPathVE: config.propertyPathVE,
                }),
              ],
            }),
          ],
        }),
      ],

    }), config))());
  }
}

export default NumberField;
