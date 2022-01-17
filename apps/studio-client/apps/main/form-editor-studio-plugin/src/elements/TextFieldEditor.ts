import SvgIconUtil from "@coremedia/studio-client.cap-base-models/util/SvgIconUtil";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import FormEditor_properties from "../bundles/FormEditor_properties";
import CheckboxField from "../fields/CheckboxField";
import NumberField from "../fields/NumberField";
import TextField from "../fields/TextField";
import AdvancedSettingsField from "../fields/advancedsettings/AdvancedSettingsField";
import Icon from "../icons/input-text.svg";
import AbstractFormElement from "./AbstractFormElement";

interface TextFieldEditorConfig extends Config<AbstractFormElement>, Partial<Pick<TextFieldEditor,
  "defaultRegexpValidatorValue" |
  "defaultName" |
  "defaultMandatory"
>> {
}

class TextFieldEditor extends AbstractFormElement {
  declare Config: TextFieldEditorConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.elements.textFieldEditor";

  static readonly FIELD_TYPE: string = "TextField";

  #defaultRegexpValidatorValue: string = null;

  get defaultRegexpValidatorValue(): string {
    return this.#defaultRegexpValidatorValue;
  }

  set defaultRegexpValidatorValue(value: string) {
    this.#defaultRegexpValidatorValue = value;
  }

  #defaultName: string = null;

  get defaultName(): string {
    return this.#defaultName;
  }

  set defaultName(value: string) {
    this.#defaultName = value;
  }

  #defaultMandatory: boolean = false;

  get defaultMandatory(): boolean {
    return this.#defaultMandatory;
  }

  set defaultMandatory(value: boolean) {
    this.#defaultMandatory = value;
  }

  constructor(config: Config<TextFieldEditor> = null) {
    super(ConfigUtils.apply(Config(TextFieldEditor, {
      formElementType: TextFieldEditor.FIELD_TYPE,
      formElementIconCls: SvgIconUtil.getIconStyleClassForSvgIcon(Icon),
      formElementGroup: "input",

      items: [
        Config(TextField, {
          fieldLabel: FormEditor_properties.FormEditor_element_name_label,
          emptyText: FormEditor_properties.FormEditor_element_name_emptyText,
          propertyName: "name",
          defaultValue: config.defaultName,
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
          defaultValue: config.defaultMandatory,
        }),
        Config(NumberField, {
          fieldLabel: FormEditor_properties.FormEditor_validator_minSize_label,
          emptyText: FormEditor_properties.FormEditor_validator_minSize_text,
          propertyName: "validator.minSize",
        }),
        Config(NumberField, {
          fieldLabel: FormEditor_properties.FormEditor_validator_maxSize_label,
          emptyText: FormEditor_properties.FormEditor_validator_maxSize_text,
          propertyName: "validator.maxSize",
        }),
        Config(TextField, {
          fieldLabel: FormEditor_properties.FormEditor_element_regexp_label,
          emptyText: FormEditor_properties.FormEditor_element_regexp_emptyText,
          propertyName: "validator.regexpValidator",
          defaultValue: config.defaultRegexpValidatorValue,
        }),
        Config(AdvancedSettingsField, { propertyName: "advancedSettings" }),
      ],
    }), config));
  }
}

export default TextFieldEditor;
