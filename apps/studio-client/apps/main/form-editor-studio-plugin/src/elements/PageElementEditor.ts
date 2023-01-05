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
import AbstractFormElement from "./AbstractFormElement";
import TextField from "../fields/TextField";
import FormEditor_properties from "../bundles/FormEditor_properties";
import ComboBoxField from "../fields/ComboBoxField";

interface PageElementEditorConfig extends Config<AbstractFormElement> {
}

class PageElementEditor extends AbstractFormElement {
  declare Config: PageElementEditorConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.elements.pageElementEditor";

  static readonly FIELD_TYPE: string = "PageElement";
  static readonly DEFAULT_PAGE: string = "DEFAULT_PAGE";
  static readonly SUMMARY_PAGE: string = "SUMMARY_PAGE";

  constructor(config: Config<PageElementEditor> = null) {
    super(ConfigUtils.apply(Config(PageElementEditor, {
      formElementType: PageElementEditor.FIELD_TYPE,
      formElementGroup: "hidden",
      items: [
        Config(TextField, {
          fieldLabel: FormEditor_properties.FormEditor_page_title_field_label,
          emptyText: FormEditor_properties.FormEditor_page_title_empty_text,
          propertyName: "name"
        }),
        Config(ComboBoxField, {
          fieldLabel: FormEditor_properties.FormEditor_page_type_field_label,
          emptyText: FormEditor_properties.FormEditor_page_type_empty_text,
          propertyName: "pageType",
          store: [
            [PageElementEditor.DEFAULT_PAGE, FormEditor_properties.FormEditor_page_type_default_label],
            [PageElementEditor.SUMMARY_PAGE, FormEditor_properties.FormEditor_page_type_summary_label]
          ],
        })
      ],
    }), config));
  }
}

export default PageElementEditor;
