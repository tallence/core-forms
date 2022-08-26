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
      //TODO: hidden field should not be listed in the applicable form elements list
      formElementGroup: "hidden",

      items: [
        Config(TextField, {
          fieldLabel: FormEditor_properties.FormEditor_page_title_field_label,
          emptyText: FormEditor_properties.FormEditor_page_title_empty_text,
          propertyName: "title"
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
