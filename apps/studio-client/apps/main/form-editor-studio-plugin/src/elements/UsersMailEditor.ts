import SvgIconUtil from "@coremedia/studio-client.cap-base-models/util/SvgIconUtil";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import FormEditor_properties from "../bundles/FormEditor_properties";
import CheckboxField from "../fields/CheckboxField";
import ComboBoxField from "../fields/ComboBoxField";
import TextField from "../fields/TextField";
import AdvancedSettingsField from "../fields/advancedsettings/AdvancedSettingsField";
import Icon from "../icons/input-mail.svg";
import AbstractFormElement from "./AbstractFormElement";

interface UsersMailEditorConfig extends Config<AbstractFormElement> {
}

class UsersMailEditor extends AbstractFormElement {
  declare Config: UsersMailEditorConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.elements.usersMailEditor";

  static readonly FIELD_TYPE: string = "UsersMail";

  protected static COMBO_VALUE_CHECKBOX: string = "CHECKBOX";

  protected static COMBO_VALUE_ALWAYS: string = "ALWAYS";

  protected static COMBO_VALUE_NEVER: string = "NEVER";

  constructor(config: Config<UsersMailEditor> = null) {
    super(ConfigUtils.apply(Config(UsersMailEditor, {
      formElementType: UsersMailEditor.FIELD_TYPE,
      formElementIconCls: SvgIconUtil.getIconStyleClassForSvgIcon(Icon),
      formElementGroup: "input",

      items: [
        Config(TextField, {
          fieldLabel: FormEditor_properties.FormEditor_element_name_label,
          emptyText: "z.B.: Ihre E-Mail Adresse",
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
        Config(ComboBoxField, {
          fieldLabel: FormEditor_properties.FormEditor_usersMail_mailType_label,
          emptyText: FormEditor_properties.FormEditor_usersMail_mailType_label,
          propertyName: "copyType",
          store: [
            [ UsersMailEditor.COMBO_VALUE_CHECKBOX, FormEditor_properties.FormEditor_usersMail_mailType_checkbox ],
            [ UsersMailEditor.COMBO_VALUE_NEVER, FormEditor_properties.FormEditor_usersMail_mailType_never ],
            [ UsersMailEditor.COMBO_VALUE_ALWAYS, FormEditor_properties.FormEditor_usersMail_mailType_always], /**/
          ],
        }),
        Config(AdvancedSettingsField, { propertyName: "advancedSettings" }),
      ],
    }), config));
  }
}

export default UsersMailEditor;
