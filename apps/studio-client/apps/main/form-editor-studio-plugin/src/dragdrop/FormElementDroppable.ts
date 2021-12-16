import ValueExpression from "@coremedia/studio-client.client-core/data/ValueExpression";
import IconDisplayField from "@coremedia/studio-client.ext.ui-components/components/IconDisplayField";
import Container from "@jangaroo/ext-ts/container/Container";
import HBoxLayout from "@jangaroo/ext-ts/layout/container/HBox";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import FormUtils from "../FormUtils";
import FormElementDroppableBase from "./FormElementDroppableBase";

interface FormElementDroppableConfig extends Config<FormElementDroppableBase>, Partial<Pick<FormElementDroppable,
  "dragActiveVE" |
  "formElementType" |
  "formElementIconCls" |
  "readOnlyVE"
>> {
}

class FormElementDroppable extends FormElementDroppableBase {
  declare Config: FormElementDroppableConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.config.formElementDroppable";

  #dragActiveVE: ValueExpression = null;

  /**
   * Stores the information whether a drag and drop operation is in progress.
   */
  get dragActiveVE(): ValueExpression {
    return this.#dragActiveVE;
  }

  set dragActiveVE(value: ValueExpression) {
    this.#dragActiveVE = value;
  }

  #formElementType: string = null;

  get formElementType(): string {
    return this.#formElementType;
  }

  set formElementType(value: string) {
    this.#formElementType = value;
  }

  #formElementIconCls: string = null;

  get formElementIconCls(): string {
    return this.#formElementIconCls;
  }

  set formElementIconCls(value: string) {
    this.#formElementIconCls = value;
  }

  #readOnlyVE: ValueExpression = null;

  get readOnlyVE(): ValueExpression {
    return this.#readOnlyVE;
  }

  set readOnlyVE(value: ValueExpression) {
    this.#readOnlyVE = value;
  }

  constructor(config: Config<FormElementDroppable> = null) {
    super(ConfigUtils.apply(Config(FormElementDroppable, {

      items: [
        Config(Container, {
          cls: "inner-component forms--applicable-element-container",
          height: 24,
          items: [
            Config(IconDisplayField, {
              iconCls: config.formElementIconCls,
              value: FormUtils.getConditionTitle(config.formElementType),
            }),
          ],
          layout: Config(HBoxLayout, { align: "middle" }),
        }),
      ],

    }), config));
  }
}

export default FormElementDroppable;
