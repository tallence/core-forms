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

import editorContext from "@coremedia/studio-client.main.editor-components/sdk/editorContext";
import PreviewPanel from "@coremedia/studio-client.main.editor-components/sdk/preview/PreviewPanel";
import Panel from "@jangaroo/ext-ts/panel/Panel";
import { as, cast } from "@jangaroo/runtime";
import FormEditor_properties from "./bundles/FormEditor_properties";
import Format from "@jangaroo/ext-ts/util/Format";
import Content from "@coremedia/studio-client.cap-rest-client/content/Content";
import ValueExpression from "@coremedia/studio-client.client-core/data/ValueExpression";
import Struct from "@coremedia/studio-client.cap-rest-client/struct/Struct";
import FormsStudioPlugin from "./FormsStudioPlugin";
import FormElementStructWrapper from "./model/FormElementStructWrapper";
import PageElementEditor from "./elements/PageElementEditor";
import FormsStudioPluginBase from "./FormsStudioPluginBase";
import ContentPropertyNames from "@coremedia/studio-client.cap-rest-client/content/ContentPropertyNames";

class FormUtils {

  static reloadPreview(): void {
    const premular = cast(Panel, editorContext._.getWorkArea().getActiveTab());
    const previewPanel = as(premular.queryById("previewPanel")[0], PreviewPanel);
    if (previewPanel && previewPanel.isInstance) {
      previewPanel.reloadFrame();
    }
  }

  /**
   * Gets the condition title based on its propertyName.
   * @param key A condition's propertyName.
   * @return Condition title.
   */
  static getConditionTitle(key: string): string {
    //message property keys are separated with '_' by convention and not with '.' ->
    // If a key contains a '.' it will be replaced.
    let keyToUse = key.replace(".", "_");
    keyToUse = keyToUse.charAt(0).toLowerCase() + keyToUse.substring(1);
    return FormEditor_properties["FormEditor_label_element_" + keyToUse] || key;
  }

  /**
   * Resolves the matching delete button tooltip based on the given disabled state.
   */
  public static getOptionRemoveButtonToolTip(disabled:Boolean):String {
    return disabled ? FormEditor_properties['FormEditor_text_add_option_disabled']
            : FormEditor_properties['FormEditor_text_add_option'];
  }

  /**
   * Validates the option, it must not be empty and must not contain dots.
   * Regarding the dots:<br>
   * the option name is currently used as the struct-property key and it can't be used for Property ValueExpressions if
   * it contain dots. A better solution would be:
   * <ol>
   *   <li>escaping the dots, or if it does not work</li>
   *   <li>save all new options in a new structure (the displayName is not used as the key of the structProperty) but stay compatible with the old structure to avoid a content-migration</li>
   * <ol>
   */
  public static validateOptionValue(option:string):Boolean {
    return option != null && Format.trim(option).length && option.indexOf(".") == -1;
  }

  /**
   * Deactivate the multi-page mode and flatten the formElements of all pages into a single list.
   * @param content the current content
   * @param self the current contents value Expression
   */
  static migrateToSinglePageForm(content: Content, self: ValueExpression<any>) {
    const formData: Struct = content.getProperties().get(FormsStudioPlugin.FORM_ELEMENTS_STRUCT_PROPERTY);
    if (formData.getType().getPropertyNames() && formData.getType().getPropertyNames().indexOf(FormElementStructWrapper.FORM_ELEMENTS_PROPERTY) != -1) {

      const formElements: Struct = formData.get(FormElementStructWrapper.FORM_ELEMENTS_PROPERTY);
      const flattenedFormElements: Record<string, any> = {};
      for (const propertyName of formElements.getType().getPropertyNames()) {
        let formElement: Struct = formElements.get(propertyName);
        if (formElement.get(FormElementStructWrapper.TYPE_PROPERTY) == PageElementEditor.FIELD_TYPE) {
          FormUtils.buildFormElementsRecord(formElement.get(FormElementStructWrapper.FORM_ELEMENTS_PROPERTY), flattenedFormElements);
        }
      }
      FormsStudioPluginBase.initInitialElements(content, flattenedFormElements);

    } else {
      FormsStudioPluginBase.initInitialElements(content);
    }
    self.extendBy(ContentPropertyNames.PROPERTIES, FormsStudioPlugin.PAGEABLE_ENABLED).setValue(0);
  }

  /**
   * Activate the multi-page mode, create one page and move all formElements into it.
   * @param content the current content
   * @param self the current contents value Expression
   */
  static migrateToMultiPageForm(content: Content, self: ValueExpression<any>) {
    const formData: Struct = content.getProperties().get(FormsStudioPlugin.FORM_ELEMENTS_STRUCT_PROPERTY);
    if (formData.getType().getPropertyNames() && formData.getType().getPropertyNames().indexOf(FormElementStructWrapper.FORM_ELEMENTS_PROPERTY) != -1) {

      const formElements: Struct = formData.get(FormElementStructWrapper.FORM_ELEMENTS_PROPERTY);
      const newElements: Record<string, any> = {};
      FormUtils.buildFormElementsRecord(formElements, newElements);
      FormsStudioPluginBase.initInitialPage(content, newElements);
    } else {
      FormsStudioPluginBase.initInitialPage(content);
    }
    self.extendBy(ContentPropertyNames.PROPERTIES, FormsStudioPlugin.PAGEABLE_ENABLED).setValue(1);
  }

  private static buildFormElementsRecord(formElements: Struct, newElements: Record<string, any>): void {
    for (const [key, value] of Object.entries(formElements.toObject())) {
      if (newElements[key] == undefined && !Array.isArray(value)) {
        newElements[key] = value;
      }
    }
  }

}

export default FormUtils;
