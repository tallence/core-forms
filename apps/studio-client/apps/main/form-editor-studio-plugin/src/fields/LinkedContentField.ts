import SingleLinkEditor from "@coremedia/studio-client.main.editor-components/sdk/premular/fields/SingleLinkEditor";
import BindDisablePlugin from "@coremedia/studio-client.main.editor-components/sdk/premular/fields/plugins/BindDisablePlugin";
import AnchorLayout from "@jangaroo/ext-ts/layout/container/Anchor";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import FormEditor_properties from "../bundles/FormEditor_properties";
import ShowFormIssuesPlugin from "../plugins/ShowFormIssuesPlugin";
import LinkedContentFieldBase from "./LinkedContentFieldBase";

interface LinkedContentFieldConfig extends Config<LinkedContentFieldBase> {
}

class LinkedContentField extends LinkedContentFieldBase {
  declare Config: LinkedContentFieldConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.editor.fields.linkedContentField";

  constructor(config: Config<LinkedContentField> = null) {
    super((()=> ConfigUtils.apply(Config(LinkedContentField, {
      width: "100%",

      /*The SingleLinkEditor needs to be wrapped in a container with AnchorLayout to be displayed correctly*/
      items: [
        Config(SingleLinkEditor, {
          linkContentType: ConfigUtils.asString(config.linkContentType ? config.linkContentType : LinkedContentFieldBase.DEFAULT_LINK_CONTENT_TYPE),
          labelAlign: "top",
          bindTo: this.getLinkTargetVE(config),
          fieldLabel: FormEditor_properties.FormEditor_element_linkTarget_label,
          ...ConfigUtils.append({
            plugins: [
              Config(BindDisablePlugin, {
                bindTo: config.bindTo,
                forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
              }),
              Config(ShowFormIssuesPlugin, {
                issuesVE: config.formIssuesVE,
                propertyName: config.propertyName,
                propertyPathVE: config.propertyPathVE,
              }),
            ],
          }),
        }),
      ],
      layout: Config(AnchorLayout),

    }), config))());
  }
}

export default LinkedContentField;
