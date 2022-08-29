import SvgIconUtil from "@coremedia/studio-client.base-models/util/SvgIconUtil";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import FormEditor_properties from "../bundles/FormEditor_properties";
import CheckboxField from "../fields/CheckboxField";
import TextField from "../fields/TextField";
import AdvancedSettingsField from "../fields/advancedsettings/AdvancedSettingsField";
import Icon from "../icons/input-iban.svg";
import AbstractFormElement from "./AbstractFormElement";

interface IbanFieldEditorConfig extends Config<AbstractFormElement> {
}

class IbanFieldEditor extends AbstractFormElement {
  declare Config: IbanFieldEditorConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.elements.ibanFieldEditor";

  static readonly FIELD_TYPE: string = "IbanField";

  protected config: Config<IbanFieldEditor> = null;

  constructor(config: Config<IbanFieldEditor> = null) {
    super(ConfigUtils.apply(Config(IbanFieldEditor, {
      formElementType: IbanFieldEditor.FIELD_TYPE,
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

        Config(CheckboxField, {
          fieldLabel: FormEditor_properties.FormEditor_element_mandatory_label,
          propertyName: "validator.mandatory",
          defaultValue: false,
        }),

        Config(AdvancedSettingsField, { propertyName: "advancedSettings" }),
      ],
    }), config));
  }
}

export default IbanFieldEditor;
