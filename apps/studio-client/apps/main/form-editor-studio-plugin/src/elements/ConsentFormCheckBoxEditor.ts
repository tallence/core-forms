import SvgIconUtil from "@coremedia/studio-client.cap-base-models/util/SvgIconUtil";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import FormEditor_properties from "../bundles/FormEditor_properties";
import CheckboxField from "../fields/CheckboxField";
import LinkedContentField from "../fields/LinkedContentField";
import TextField from "../fields/TextField";
import AdvancedSettingsField from "../fields/advancedsettings/AdvancedSettingsField";
import Icon from "../icons/confirm.svg";
import AbstractFormElement from "./AbstractFormElement";

interface ConsentFormCheckBoxEditorConfig extends Config<AbstractFormElement> {
}

class ConsentFormCheckBoxEditor extends AbstractFormElement {
  declare Config: ConsentFormCheckBoxEditorConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.elements.consentFormCheckBoxEditor";

  static readonly FIELD_TYPE: string = "ConsentFormCheckBox";

  constructor(config: Config<ConsentFormCheckBoxEditor> = null) {
    super(ConfigUtils.apply(Config(ConsentFormCheckBoxEditor, {
      formElementType: ConsentFormCheckBoxEditor.FIELD_TYPE,
      formElementIconCls: SvgIconUtil.getIconStyleClassForSvgIcon(Icon),

      items: [
        Config(TextField, {
          fieldLabel: FormEditor_properties.FormEditor_element_name_label,
          emptyText: FormEditor_properties.FormEditor_element_name_emptyText,
          propertyName: "name",
        }),
        Config(TextField, {
          fieldLabel: FormEditor_properties.FormEditor_element_hint_label,
          emptyText: FormEditor_properties.FormEditor_element_hint_linkTarget_label,
          propertyName: "hint",
        }),
        Config(CheckboxField, {
          fieldLabel: FormEditor_properties.FormEditor_element_mandatory_label,
          propertyName: "validator.mandatory",
          defaultValue: false,
        }),
        Config(LinkedContentField, { propertyName: "linkTarget" }),
        Config(AdvancedSettingsField, { propertyName: "advancedSettings" }),
      ],
    }), config));
  }
}

export default ConsentFormCheckBoxEditor;
