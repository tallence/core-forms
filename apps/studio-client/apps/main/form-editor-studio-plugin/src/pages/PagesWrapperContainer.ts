/*
 * Copyright 2018 Tallence AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
  #activeTabVE: ValueExpression = null;

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

  getActiveTabVE(): ValueExpression {
    if (!this.#activeTabVE) {
      this.#activeTabVE = ValueExpressionFactory.createFromValue(null);
    }
    return this.#activeTabVE;
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
          afterUpdateCallback: this.onPageUpdate,
          template: Config(FormPageTab, {
            forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
            bindTo: config.bindTo,
            formElementsManager: config.formElementsManager,
            activeTabValueExpression: this.getActiveTabVE(),
          }),
        }),
      ],
      items: [
        /* elements will be added dynamically*/
      ],

    }), config))());
  }

  onPageUpdate(): void {
    let activeTab = this.getActiveTabVE().getValue();
    if (activeTab) {
      this.setActiveTab(activeTab);
    } else {
      this.setActiveTab(0);
    }
  }
}

export default PagesWrapperContainer;
