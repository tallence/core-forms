import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import TabPanel from "@jangaroo/ext-ts/tab/Panel";
import FormPageTab from "./FormPageTab";
import BEMPlugin from "@coremedia/studio-client.ext.ui-components/plugins/BEMPlugin";
import ValueExpression from "@coremedia/studio-client.client-core/data/ValueExpression";
import FormElementsManager from "../helper/FormElementsManager";
import ValueExpressionFactory from "@coremedia/studio-client.client-core/data/ValueExpressionFactory";
import FormElementStructWrapper from "../model/FormElementStructWrapper";
import BindComponentsPlugin from "@coremedia/studio-client.ext.ui-components/plugins/BindComponentsPlugin";
import PageElementEditor from "../elements/PageElementEditor";

interface PagesWrapperContainerConfig extends Config<TabPanel>, Partial<Pick<PagesWrapperContainer,
        "bindTo" |
        "forceReadOnlyValueExpression" |
        "formElementsManager">> {
}

class PagesWrapperContainer extends TabPanel {

  static override readonly xtype: string = "com.tallence.formeditor.studio.config.pagesWrapperContainer";

  declare Config: PagesWrapperContainerConfig;

  #bindTo: ValueExpression = null;
  #forceReadOnlyValueExpression: ValueExpression = null;
  #formElementsManager: FormElementsManager = null;

  get bindTo(): ValueExpression {
    return this.#bindTo;
  }

  get forceReadOnlyValueExpression(): ValueExpression {
    return this.#forceReadOnlyValueExpression;
  }

  get formElementsManager(): FormElementsManager {
    return this.#formElementsManager;
  }

  getPages(config: PagesWrapperContainerConfig): ValueExpression {
    return ValueExpressionFactory.createTransformingValueExpression(config.formElementsManager.getFormElementsVE(), ((elements: Array<FormElementStructWrapper>): Array<FormElementStructWrapper> =>
                    elements.filter((element: FormElementStructWrapper): boolean => element.getType() == PageElementEditor.FIELD_TYPE)
    ))
  }

  constructor(config: Config<PagesWrapperContainer> = null) {
    super((() => ConfigUtils.apply(Config(PagesWrapperContainer, {
      bodyPadding: 20,
      plugins: [
        Config(BEMPlugin, {
          block: "form-page-container",
        }),
        Config(BindComponentsPlugin, {
          configBeanParameterName: "page",
          clearBeforeUpdate: true,
          valueExpression: this.getPages(config),
          template: Config(FormPageTab, {
            forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
            bindTo: config.bindTo,
            formElementsManager: config.formElementsManager,
          }),
        }),
      ],
      items: [
        /* elements will be added dynamically*/
      ],

    }), config))());
  }

}

export default PagesWrapperContainer;
