import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import FormEditor_properties from "../bundles/FormEditor_properties";
import AddOptionField from "../fields/AddOptionField";
import CheckboxField from "../fields/CheckboxField";
import TextField from "../fields/TextField";
import AdvancedSettingsField from "../fields/advancedsettings/AdvancedSettingsField";
import AbstractFormElement from "./AbstractFormElement";

interface RadioButtonsEditorConfig extends Config<AbstractFormElement> {
}

class RadioButtonsEditor extends AbstractFormElement {
  declare Config: RadioButtonsEditorConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.elements.radioButtonsEditor";

  static readonly FIELD_TYPE: string = "RadioButtons";

  constructor(config: Config<RadioButtonsEditor> = null) {
    super(ConfigUtils.apply(Config(RadioButtonsEditor, {
      formElementType: RadioButtonsEditor.FIELD_TYPE,
      formElementIconCls: "tallence-icons tallence-icons--radio-group",
      formElementGroup: "multiple",

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
        Config(CheckboxField, {
          fieldLabel: FormEditor_properties.FormEditor_element_mandatory_label,
          propertyName: "validator.mandatory",
          defaultValue: false,
        }),
        Config(AddOptionField, { propertyName: "groupElements" }),
        Config(AdvancedSettingsField, { propertyName: "advancedSettings" }),
      ],
    }), config));
  }
}

export default RadioButtonsEditor;
