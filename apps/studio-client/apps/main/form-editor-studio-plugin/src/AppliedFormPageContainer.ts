import ValueExpressionFactory from "@coremedia/studio-client.client-core/data/ValueExpressionFactory";
import BindPropertyPlugin from "@coremedia/studio-client.ext.ui-components/plugins/BindPropertyPlugin";
import {bind} from "@jangaroo/runtime";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import StateFulCollapsiblePanel from "./components/StateFulCollapsiblePanel";
import ShowFormIssuesPlugin from "./plugins/ShowFormIssuesPlugin";
import AppliedFormPageContainerBase from "./AppliedFormPageContainerBase";

interface AppliedFormPageContainerConfig extends Config<AppliedFormPageContainerBase> {
}

class AppliedFormPageContainer extends AppliedFormPageContainerBase {
  declare Config: AppliedFormPageContainerConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.config.appliedFormPageContainer";

  constructor(config: Config<AppliedFormPageContainer> = null) {
    super((() => ConfigUtils.apply(Config(AppliedFormPageContainer, {

      items: [
        /* We need to overwrite the collapsedCls. Otherwise the header would have a transparent background.  */
        Config(StateFulCollapsiblePanel, {
          margin: "10 15 0 10",
          collapsible: false,
          itemId: AppliedFormPageContainerBase.FORM_ELEMENT_PANEL,
          collapsedCls: "collapsed-form-element",
          animCollapse: false,
          title: "Page Eigenschaften",
          plugins: [
            Config(BindPropertyPlugin, {
              bidirectional: false,
              componentProperty: "iconCls",
              transformer: bind(this, this.iconClassTransformer),
              bindTo: config.formElement.getFormElementVE().extendBy("type"),
            }),
            Config(ShowFormIssuesPlugin, {
              issuesVE: config.bindTo.extendBy(["issues"]),
              propertyPathVE: ValueExpressionFactory.createFromValue(config.formElement.getPropertyPath()),
            }),
          ],
          items: [
            /* The form element editor is added dynamically bye the
        {@link com.coremedia.ui.util.IReusableComponentsService}  */
          ],
        })
      ],
    }), config))());
  }
}

export default AppliedFormPageContainer;
