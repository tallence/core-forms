import ValueExpressionFactory from "@coremedia/studio-client.client-core/data/ValueExpressionFactory";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import StateFulCollapsiblePanel from "./components/StateFulCollapsiblePanel";
import ShowFormIssuesPlugin from "./plugins/ShowFormIssuesPlugin";
import AppliedFormPageContainerBase from "./AppliedFormPageContainerBase";
import PageElementEditor from "./elements/PageElementEditor";

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
            Config(ShowFormIssuesPlugin, {
              issuesVE: config.bindTo.extendBy(["issues"]),
              propertyPathVE: ValueExpressionFactory.createFromValue(config.formElement.getPropertyPath()),
            }),
          ],
          items: [
            Config(PageElementEditor, {
              itemId: AppliedFormPageContainerBase.FORM_PAGE_EDITOR,
              bindTo: config.bindTo,
              forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
              formElement: config.formElement
            })
          ],
        })
      ],
    }), config))());
  }
}

export default AppliedFormPageContainer;
