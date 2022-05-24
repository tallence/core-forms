import IconButton from "@coremedia/studio-client.ext.ui-components/components/IconButton";
import StatefulTextField from "@coremedia/studio-client.ext.ui-components/components/StatefulTextField";
import BindPropertyPlugin from "@coremedia/studio-client.ext.ui-components/plugins/BindPropertyPlugin";
import ButtonSkin from "@coremedia/studio-client.ext.ui-components/skins/ButtonSkin";
import DisplayFieldSkin from "@coremedia/studio-client.ext.ui-components/skins/DisplayFieldSkin";
import Editor_properties from "@coremedia/studio-client.main.editor-components/Editor_properties";
import CollapsibleFormPanel from "@coremedia/studio-client.main.editor-components/sdk/premular/CollapsibleFormPanel";
import FormSpacerElement from "@coremedia/studio-client.main.editor-components/sdk/premular/fields/FormSpacerElement";
import Button from "@jangaroo/ext-ts/button/Button";
import FieldContainer from "@jangaroo/ext-ts/form/FieldContainer";
import Checkbox from "@jangaroo/ext-ts/form/field/Checkbox";
import DisplayField from "@jangaroo/ext-ts/form/field/Display";
import Fill from "@jangaroo/ext-ts/toolbar/Fill";
import Toolbar from "@jangaroo/ext-ts/toolbar/Toolbar";
import { bind } from "@jangaroo/runtime";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import resourceManager from "@jangaroo/runtime/l10n/resourceManager";
import FormEditor_properties from "../bundles/FormEditor_properties";
import EditOptionWindowBase from "./EditOptionWindowBase";
import FormUtils from "../FormUtils";

interface EditOptionWindowConfig extends Config<EditOptionWindowBase> {
}

class EditOptionWindow extends EditOptionWindowBase {
  declare Config: EditOptionWindowConfig;

  static readonly #FE: string = "com.tallence.formeditor.studio.bundles.FormEditor";

  static override readonly xtype: string = "com.tallence.formeditor.studio.editor.fields.editOptionWindow";

  constructor(config: Config<EditOptionWindow> = null) {
    super((()=> ConfigUtils.apply(Config(EditOptionWindow, {
      resizable: false,
      modal: true,
      items: [
        Config(CollapsibleFormPanel, {
          collapsible: false,
          items: [

            Config(FieldContainer, {
              fieldLabel: resourceManager.getString(EditOptionWindow.#FE, "FormEditor_element_group_labelField_text"),
              items: [
                Config(StatefulTextField, {

                  width: "100%",
                  emptyText: resourceManager.getString(EditOptionWindow.#FE, "FormEditor_element_group_labelField_emptyText"),
                  plugins: [
                    Config(BindPropertyPlugin, {
                      bidirectional: true,
                      componentProperty: "highlighted",
                      bindTo: this.getSaveButtonDisabledVE(),
                    }),
                    Config(BindPropertyPlugin, {
                      bidirectional: true,
                      bindTo: this.getOptionNameVE(),
                    }),
                  ],
                }),
              ],
            }),

            Config(FormSpacerElement, { height: "10px" }),

            Config(FieldContainer, {
              fieldLabel: resourceManager.getString(EditOptionWindow.#FE, "FormEditor_element_group_valueField_text"),
              items: [
                Config(StatefulTextField, {
                  width: "100%",
                  emptyText: resourceManager.getString(EditOptionWindow.#FE, "FormEditor_element_group_valueField_emptyText"),
                  plugins: [
                    Config(BindPropertyPlugin, {
                      bidirectional: true,
                      bindTo: this.getOptionValueVE(),
                    }),
                  ],
                }),

                Config(DisplayField, {
                  value: resourceManager.getString(EditOptionWindow.#FE, "FormEditor_element_group_valueField_hint"),
                  ui: DisplayFieldSkin.ITALIC.getSkin(),
                }),
              ],
            }),

            Config(FormSpacerElement, { height: "20px" }),

            Config(Checkbox, {
              fieldLabel: resourceManager.getString(EditOptionWindow.#FE, "FormEditor_element_group_default_tooltip"),
              plugins: [
                Config(BindPropertyPlugin, {
                  bidirectional: true,
                  skipIfUndefined: true,
                  bindTo: this.getOptionCheckedVE(),
                }),
              ],
            }),
          ],
        }),

      ],
      fbar: Config(Toolbar, {
        items: [
          Config(IconButton, {
            iconAlign: "right",
            iconCls: Editor_properties.lifecycleStatus_deleted_icon,
            itemId: "removeButton",
            ariaLabel: FormEditor_properties.FormEditor_text_delete_option,
            handler: bind(this, this.removeOption),
          }),
          Config(Fill),
          Config(Button, {
            ui: ButtonSkin.FOOTER_PRIMARY.getSkin(),
            text: resourceManager.getString(EditOptionWindow.#FE, "FormEditor_text_save_option"),
            handler: bind(this, this.saveOption),
            scale: "small",
            plugins: [
              Config(BindPropertyPlugin, {
                componentProperty: "disabled",
                bindTo: this.getSaveButtonDisabledVE(),
              }),
              Config(BindPropertyPlugin, {
                componentProperty: "tooltip",
                transformer: function(disabled:Boolean):String { return FormUtils.getOptionRemoveButtonToolTip(disabled); },
                bindTo: this.getSaveButtonDisabledVE(),
              }),
            ],
          }),

          Config(Button, {
            ui: ButtonSkin.FOOTER_SECONDARY.getSkin(),
            text: Editor_properties.dialog_defaultCancelButton_text,
            handler: bind(this, this.close),
            scale: "small",
          }),
        ],
      }),

    }), config))());
  }
}

export default EditOptionWindow;
