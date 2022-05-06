import SvgIconUtil from "@coremedia/studio-client.base-models/util/SvgIconUtil";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import FormEditor_properties from "../bundles/FormEditor_properties";
import CheckboxField from "../fields/CheckboxField";
import NumberField from "../fields/NumberField";
import TextField from "../fields/TextField";
import AdvancedSettingsField from "../fields/advancedsettings/AdvancedSettingsField";
import Icon from "../icons/input-number.svg";
import AbstractFormElement from "./AbstractFormElement";

interface NumberFieldEditorConfig extends Config<AbstractFormElement> {
}

class NumberFieldEditor extends AbstractFormElement {
  declare Config: NumberFieldEditorConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.elements.numberFieldEditor";

  static readonly FIELD_TYPE: string = "NumberField";

  constructor(config: Config<NumberFieldEditor> = null) {
    super(ConfigUtils.apply(Config(NumberFieldEditor, {
      formElementType: NumberFieldEditor.FIELD_TYPE,
      formElementIconCls: SvgIconUtil.getIconStyleClassForSvgIcon(Icon),
      formElementGroup: "input",

      items: [
        Config(TextField, {
          fieldLabel: FormEditor_properties.FormEditor_element_name_label,
          emptyText: FormEditor_properties.FormEditor_element_name_emptyText,
          propertyName: "name",
        }),
        Config(TextField, {
          fieldLabel: FormEditor_properties.FormEditor_element_hint_label,
          emptyText: FormEditor_properties.FormEditor_element_hint_emptyText,
          propertyName: "hint",
        }),
        Config(TextField, {
          fieldLabel: FormEditor_properties.FormEditor_element_placeholder_label,
          emptyText: FormEditor_properties.FormEditor_element_placeholder_emptyText,
          propertyName: "placeholder",
        }),
        Config(CheckboxField, {
          fieldLabel: FormEditor_properties.FormEditor_element_mandatory_label,
          propertyName: "validator.mandatory",
          defaultValue: false,
        }),
        Config(NumberField, {
          fieldLabel: FormEditor_properties.FormEditor_validator_minValue_label,
          emptyText: FormEditor_properties.FormEditor_validator_minValue_text,
          propertyName: "validator.minSize",
        }),
        Config(NumberField, {
          fieldLabel: FormEditor_properties.FormEditor_validator_maxValue_label,
          emptyText: FormEditor_properties.FormEditor_validator_maxValue_text,
          propertyName: "validator.maxSize",
        }),
        Config(AdvancedSettingsField, { propertyName: "advancedSettings" }),
      ],
    }), config));
  }
}

export default NumberFieldEditor;
