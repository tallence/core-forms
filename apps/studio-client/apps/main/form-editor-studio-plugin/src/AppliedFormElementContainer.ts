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

import ValueExpressionFactory from "@coremedia/studio-client.client-core/data/ValueExpressionFactory";
import IconButton from "@coremedia/studio-client.ext.ui-components/components/IconButton";
import BindPropertyPlugin from "@coremedia/studio-client.ext.ui-components/plugins/BindPropertyPlugin";
import Editor_properties from "@coremedia/studio-client.main.editor-components/Editor_properties";
import BindDisablePlugin from "@coremedia/studio-client.main.editor-components/sdk/premular/fields/plugins/BindDisablePlugin";
import PanelHeader from "@jangaroo/ext-ts/panel/Header";
import { bind } from "@jangaroo/runtime";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import AppliedFormElementsContainerBase from "./AppliedFormElementsContainerBase";
import FormEditor_properties from "./bundles/FormEditor_properties";
import StateFulCollapsiblePanel from "./components/StateFulCollapsiblePanel";
import FormElementDropContainer from "./dragdrop/FormElementDropContainer";
import ShowFormIssuesPlugin from "./plugins/ShowFormIssuesPlugin";

interface AppliedFormElementContainerConfig extends Config<AppliedFormElementsContainerBase> {
}

class AppliedFormElementContainer extends AppliedFormElementsContainerBase {
  declare Config: AppliedFormElementContainerConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.config.appliedFormElementContainer";

  constructor(config: Config<AppliedFormElementContainer> = null) {
    super((()=> ConfigUtils.apply(Config(AppliedFormElementContainer, {

      items: [
        /* We need to overwrite the collapsedCls. Otherwise the header would have a transparent background.  */
        Config(StateFulCollapsiblePanel, {
          margin: "10 15 0 10",
          itemId: AppliedFormElementsContainerBase.FORM_ELEMENT_PANEL,
          collapsedCls: "collapsed-form-element",
          animCollapse: false,
          plugins: [
            Config(BindPropertyPlugin, {
              bidirectional: false,
              componentProperty: "collapsed",
              transformer: bind(this, this.collapsedTransformer),
              bindTo: config.formElementsManager.getCollapsedElementVE(),
            }),
            Config(BindPropertyPlugin, {
              bidirectional: false,
              componentProperty: "title",
              ifUndefined: AppliedFormElementsContainerBase.getTitleUndefinedValue(config.formElement),
              transformer: bind(this, this.titleTransformer),
              bindTo: config.formElement.getFormElementVE().extendBy("name"),
            }),
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
          header:
        /*Using focusableContainer(cm9-17 style) and enableFocusableContainer(cm9-18 style) to be compatible with all cm9-versions*/
        Config(PanelHeader, {
          titlePosition: 2.0,
          itemId: AppliedFormElementsContainerBase.FORM_ELEMENT_HEADER,
          focusableContainer: false,
          ...{ enableFocusableContainer: false },
          itemPosition: 2,
          items: [
            Config(IconButton, {
              iconCls: Editor_properties.lifecycleStatus_deleted_icon,
              itemId: "removeButton",
              ariaLabel: FormEditor_properties.FormEditor_tooltip_deletethis,
              handler: bind(this, this.removeElementHandler),
              plugins: [
                Config(BindDisablePlugin, {
                  bindTo: config.bindTo,
                  forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
                }),
              ],
            }),
          ],
        }),
        }),
        Config(FormElementDropContainer, {
          formElementsManager: config.formElementsManager,
          formElementId: config.formElement.getId(),
          forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
        }),

      ],

    }), config))());
  }
}

export default AppliedFormElementContainer;
