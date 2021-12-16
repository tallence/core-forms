import SvgIconUtil from "@coremedia/studio-client.cap-base-models/util/SvgIconUtil";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import FormEditor_properties from "../bundles/FormEditor_properties";
import TextField from "../fields/TextField";
import AdvancedSettingsField from "../fields/advancedsettings/AdvancedSettingsField";
import Icon from "../icons/infotext.svg";
import AbstractFormElement from "./AbstractFormElement";

interface TextOnlyEditorConfig extends Config<AbstractFormElement> {
}

class TextOnlyEditor extends AbstractFormElement {
  declare Config: TextOnlyEditorConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.elements.textOnlyEditor";

  static readonly FIELD_TYPE: string = "TextOnly";

  constructor(config: Config<TextOnlyEditor> = null) {
    super(ConfigUtils.apply(Config(TextOnlyEditor, {
      formElementType: TextOnlyEditor.FIELD_TYPE,
      formElementIconCls: SvgIconUtil.getIconStyleClassForSvgIcon(Icon),

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
        Config(AdvancedSettingsField, { propertyName: "advancedSettings" }),
      ],
    }), config));
  }
}

export default TextOnlyEditor;
