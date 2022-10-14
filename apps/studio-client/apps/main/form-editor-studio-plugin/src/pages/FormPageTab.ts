import Config from "@jangaroo/runtime/Config";
import Panel from "@jangaroo/ext-ts/panel/Panel";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import ValueExpression from "@coremedia/studio-client.client-core/data/ValueExpression";
import FormElementsManager from "../helper/FormElementsManager";
import AppliedElementsContainer from "../AppliedElementsContainer";
import FormElementStructWrapper from "../model/FormElementStructWrapper";
import BindPropertyPlugin from "@coremedia/studio-client.ext.ui-components/plugins/BindPropertyPlugin";
import Container from "@jangaroo/ext-ts/container/Container";
import AppliedFormPageContainer from "../AppliedFormPageContainer";
import HBoxLayout from "@jangaroo/ext-ts/layout/container/HBox";
import {bind} from "@jangaroo/runtime";
import IconButton from "@coremedia/studio-client.ext.ui-components/components/IconButton";
import CoreIcons_properties from "@coremedia/studio-client.core-icons/CoreIcons_properties";


interface FormPageTabConfig extends Config<Panel>, Partial<Pick<FormPageTab,
        "bindTo" |
        "forceReadOnlyValueExpression" |
        "page" |
        "activeTabValueExpression" |
        "formElementsManager">> {
}

/**
 * TODO: implement all actions / button handlers
 */
class FormPageTab extends Panel {
  declare Config: FormPageTabConfig;
  static override readonly xtype: string = "com.tallence.formeditor.studio.config.formPageTab";

  #bindTo: ValueExpression = null;
  #forceReadOnlyValueExpression: ValueExpression = null;
  #formElementsManager: FormElementsManager = null;
  #page: FormElementStructWrapper = null;
  #activeTabValueExpression: ValueExpression = null;

  get page(): FormElementStructWrapper {
    return this.#page;
  }

  get activeTabValueExpression(): ValueExpression {
    return this.#activeTabValueExpression;
  }

  get bindTo(): ValueExpression {
    return this.#bindTo;
  }

  get forceReadOnlyValueExpression(): ValueExpression {
    return this.#forceReadOnlyValueExpression;
  }

  get formElementsManager(): FormElementsManager {
    return this.#formElementsManager;
  }

  addPage() {
    let newPageId:String = this.#formElementsManager.addFormPage(this.page.getId());
    if (this.#activeTabValueExpression){
      this.#activeTabValueExpression.setValue(this.buildPageId(newPageId));
    }
  }

  removePage() {
    this.#formElementsManager.removeFormElement(this.page.getId())
  }

  buildPageId(structId: String): string {
    return "page-" + structId;
  }

  constructor(config: Config<FormPageTab> = null) {
    super((() => ConfigUtils.apply(Config(FormPageTab, {
      title: config.page.getNode().getValueAsStruct().get("name"),
      itemId: this.buildPageId(config.page.getId()),
      plugins: [
        Config(BindPropertyPlugin, {
          bidirectional: false,
          componentProperty: "title",
          bindTo: config.page.getFormElementVE().extendBy("name"),
        })
      ],
      dockedItems: [
        Config(Container, {
          items: [
            Config(Container, {
              layout: Config(HBoxLayout),
              items: [
                Config(IconButton, {
                  iconCls: CoreIcons_properties.add,
                  text: "<",
                  handler: bind(this, this.addPage)
                }),
                Config(IconButton, {
                  iconCls: CoreIcons_properties.arrow_left,
                  handler: bind(this, this.addPage)
                }),
                Config(IconButton, {
                  iconCls: CoreIcons_properties.remove,
                  handler: bind(this, this.removePage)
                }),
                Config(IconButton, {
                  iconCls: CoreIcons_properties.arrow_right,
                  handler: bind(this, this.addPage)
                }),
                Config(IconButton, {
                  iconCls: CoreIcons_properties.add,
                  text: ">",
                  handler: bind(this, this.addPage)
                })
              ]
            }),
            Config(AppliedFormPageContainer, {
              bindTo: config.bindTo,
              formElement: config.page,
              forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
              formElementsManager: config.formElementsManager,
            }),
          ]
        })
      ],
      items: [
        Config(AppliedElementsContainer, {
          bindTo: config.bindTo,
          forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
          formElementsManager: new FormElementsManager(config.bindTo, config.forceReadOnlyValueExpression, "formData.formElements." + config.page.getId())
        })
      ],

    }), config))());
    this.#formElementsManager = config.formElementsManager;
    this.#page = config.page;
    this.#activeTabValueExpression = config.activeTabValueExpression;
  }
}

export default FormPageTab;
