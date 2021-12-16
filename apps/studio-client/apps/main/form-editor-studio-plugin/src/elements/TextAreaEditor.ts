import SvgIconUtil from "@coremedia/studio-client.cap-base-models/util/SvgIconUtil";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import FormEditor_properties from "../bundles/FormEditor_properties";
import CheckboxField from "../fields/CheckboxField";
import NumberField from "../fields/NumberField";
import TextField from "../fields/TextField";
import AdvancedSettingsField from "../fields/advancedsettings/AdvancedSettingsField";
import Icon from "../icons/text-area.svg";
import AbstractFormElement from "./AbstractFormElement";

interface TextAreaEditorConfig extends Config<AbstractFormElement> {
}

class TextAreaEditor extends AbstractFormElement {
  declare Config: TextAreaEditorConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.elements.textAreaEditor";

  static readonly FIELD_TYPE: string = "TextArea";

  constructor(config: Config<TextAreaEditor> = null) {
    super(ConfigUtils.apply(Config(TextAreaEditor, {
      formElementType: TextAreaEditor.FIELD_TYPE,
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
          fieldLabel: FormEditor_properties.FormEditor_validator_maxSize_label,
          emptyText: FormEditor_properties.FormEditor_validator_maxSize_text,
          propertyName: "validator.maxSize",
        }),
        Config(NumberField, {
          fieldLabel: FormEditor_properties.FormEditor_fields_textArea_rows_label,
          emptyText: FormEditor_properties.FormEditor_fields_textArea_rows_text,
          propertyName: "rows",
        }),
        Config(AdvancedSettingsField, { propertyName: "advancedSettings" }),
      ],
    }), config));
  }
}

export default TextAreaEditor;
