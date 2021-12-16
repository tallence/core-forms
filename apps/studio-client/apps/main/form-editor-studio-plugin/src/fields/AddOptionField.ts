import IconButton from "@coremedia/studio-client.ext.ui-components/components/IconButton";
import StatefulTextField from "@coremedia/studio-client.ext.ui-components/components/StatefulTextField";
import BindComponentsPlugin from "@coremedia/studio-client.ext.ui-components/plugins/BindComponentsPlugin";
import BindPropertyPlugin from "@coremedia/studio-client.ext.ui-components/plugins/BindPropertyPlugin";
import Editor_properties from "@coremedia/studio-client.main.editor-components/Editor_properties";
import BindDisablePlugin from "@coremedia/studio-client.main.editor-components/sdk/premular/fields/plugins/BindDisablePlugin";
import Container from "@jangaroo/ext-ts/container/Container";
import FieldContainer from "@jangaroo/ext-ts/form/FieldContainer";
import HBoxLayout from "@jangaroo/ext-ts/layout/container/HBox";
import { bind } from "@jangaroo/runtime";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import FormEditor_properties from "../bundles/FormEditor_properties";
import ElementGroupEntry from "../elements/ElementGroupEntry";
import ShowFormIssuesPlugin from "../plugins/ShowFormIssuesPlugin";
import AddOptionFieldBase from "./AddOptionFieldBase";

interface AddOptionFieldConfig extends Config<AddOptionFieldBase> {
}

class AddOptionField extends AddOptionFieldBase {
  declare Config: AddOptionFieldConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.editor.fields.addOptionField";

  constructor(config: Config<AddOptionField> = null) {
    super((()=> ConfigUtils.apply(Config(AddOptionField, {

      items: [

        Config(FieldContainer, {
          fieldLabel: FormEditor_properties.FormEditor_text_label_option,
          items: [

            Config(Container, {
              items: [
                Config(StatefulTextField, {
                  flex: 1,
                  emptyText: FormEditor_properties.FormEditor_text_add_option,
                  plugins: [
                    Config(BindDisablePlugin, {
                      bindTo: config.bindTo,
                      forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
                    }),
                    Config(BindPropertyPlugin, {
                      bidirectional: true,
                      skipIfUndefined: true,
                      bindTo: this.getAddOptionVE(),
                    }),
                    Config(ShowFormIssuesPlugin, {
                      issuesVE: config.formIssuesVE,
                      propertyName: config.propertyName,
                      propertyPathVE: config.propertyPathVE,
                    }),
                  ],
                }),
                Config(IconButton, {
                  iconCls: Editor_properties.LinkListPropertyField_icon,
                  ariaLabel: FormEditor_properties.FormEditor_text_add_option,
                  handler: bind(this, this.addGroupElement),
                  plugins: [
                    Config(BindDisablePlugin, {
                      bindTo: config.bindTo,
                      forceReadOnlyValueExpression: this.getAddOptionButtonDisabledVE(),
                    }),
                  ],
                }),
              ],
              layout: Config(HBoxLayout, { align: "stretch" }),
            }),

          ],

        }),

        Config(Container, {
          width: "100%",
          padding: "0 0 0 105",
          plugins: [
            Config(BindComponentsPlugin, {
              configBeanParameterName: "option",
              valueExpression: this.getGroupElementsVE(config),
              template: Config(ElementGroupEntry, {
                forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
                bindTo: config.bindTo,
                removeGroupElementFn: bind(this, this.removeGroupElement),
                updateOptionElementFn: bind(this, this.updateGroupElement),
                moveOptionElementFn: bind(this, this.moveGroupElement),
              }),
            }),
          ],
        }),

      ],
    }), config))());
  }
}

export default AddOptionField;
