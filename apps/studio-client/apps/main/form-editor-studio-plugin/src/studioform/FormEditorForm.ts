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

import BlueprintTabs_properties from "@coremedia-blueprint/studio-client.main.blueprint-forms/BlueprintTabs_properties";
import DetailsDocumentForm
  from "@coremedia-blueprint/studio-client.main.blueprint-forms/forms/containers/DetailsDocumentForm";
import ExternallyVisibleDateForm
  from "@coremedia-blueprint/studio-client.main.blueprint-forms/forms/containers/ExternallyVisibleDateForm";
import LinkedSettingsForm
  from "@coremedia-blueprint/studio-client.main.blueprint-forms/forms/containers/LinkedSettingsForm";
import LocalSettingsForm
  from "@coremedia-blueprint/studio-client.main.blueprint-forms/forms/containers/LocalSettingsForm";
import MediaDocumentForm
  from "@coremedia-blueprint/studio-client.main.blueprint-forms/forms/containers/MediaDocumentForm";
import MultiLanguageDocumentForm
  from "@coremedia-blueprint/studio-client.main.blueprint-forms/forms/containers/MultiLanguageDocumentForm";
import RelatedDocumentForm
  from "@coremedia-blueprint/studio-client.main.blueprint-forms/forms/containers/RelatedDocumentForm";
import SearchableForm from "@coremedia-blueprint/studio-client.main.blueprint-forms/forms/containers/SearchableForm";
import TeaserDocumentForm
  from "@coremedia-blueprint/studio-client.main.blueprint-forms/forms/containers/TeaserDocumentForm";
import ValidityDocumentForm
  from "@coremedia-blueprint/studio-client.main.blueprint-forms/forms/containers/ValidityDocumentForm";
import ViewTypeSelectorForm
  from "@coremedia-blueprint/studio-client.main.blueprint-forms/forms/containers/ViewTypeSelectorForm";
import MetaDataDocumentForm
  from "@coremedia-blueprint/studio-client.main.blueprint-forms/forms/media/MetaDataDocumentForm";
import SvgIconUtil from "@coremedia/studio-client.base-models/util/SvgIconUtil";
import LocalComboBox from "@coremedia/studio-client.ext.ui-components/components/LocalComboBox";
import BindPropertyPlugin from "@coremedia/studio-client.ext.ui-components/plugins/BindPropertyPlugin";
import DocumentForm from "@coremedia/studio-client.main.editor-components/sdk/premular/DocumentForm";
import DocumentInfo from "@coremedia/studio-client.main.editor-components/sdk/premular/DocumentInfo";
import DocumentMetaDataFormDispatcher
  from "@coremedia/studio-client.main.editor-components/sdk/premular/DocumentMetaDataFormDispatcher";
import DocumentTabPanel from "@coremedia/studio-client.main.editor-components/sdk/premular/DocumentTabPanel";
import PropertyFieldGroup from "@coremedia/studio-client.main.editor-components/sdk/premular/PropertyFieldGroup";
import ReferrerListPanel from "@coremedia/studio-client.main.editor-components/sdk/premular/ReferrerListPanel";
import VersionHistory from "@coremedia/studio-client.main.editor-components/sdk/premular/VersionHistory";
import BooleanPropertyField
  from "@coremedia/studio-client.main.editor-components/sdk/premular/fields/BooleanPropertyField";
import StringPropertyField
  from "@coremedia/studio-client.main.editor-components/sdk/premular/fields/StringPropertyField";
import StructPropertyField
  from "@coremedia/studio-client.main.editor-components/sdk/premular/fields/struct/StructPropertyField";
import JsonStore from "@jangaroo/ext-ts/data/JsonStore";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import FormEditorDocumentForm from "../FormEditorDocumentForm";
import FormContentTypes_properties from "../bundles/FormContentTypes_properties";
import FormEditor_properties from "../bundles/FormEditor_properties";
import CheckBoxesEditor from "../elements/CheckBoxesEditor";
import ConsentFormCheckBoxEditor from "../elements/ConsentFormCheckBoxEditor";
import DateFieldEditor from "../elements/DateFieldEditor";
import FileUploadEditor from "../elements/FileUploadEditor";
import NumberFieldEditor from "../elements/NumberFieldEditor";
import RadioButtonsEditor from "../elements/RadioButtonsEditor";
import SelectBoxEditor from "../elements/SelectBoxEditor";
import TextAreaEditor from "../elements/TextAreaEditor";
import TextFieldEditor from "../elements/TextFieldEditor";
import TextOnlyEditor from "../elements/TextOnlyEditor";
import UsersMailEditor from "../elements/UsersMailEditor";
import InputCity from "../icons/input-city-zip.svg";
import InputFax from "../icons/input-fax.svg";
import InputPhone from "../icons/input-phone.svg";
import InputStreet from "../icons/input-street-nr.svg";
import HiddenFieldEditor from "../elements/HiddenFieldEditor";
import IbanFieldEditor from "../elements/IbanFieldEditor";
import FormsStudioPlugin from "../FormsStudioPlugin";
import Content from "@coremedia/studio-client.cap-rest-client/content/Content";
import ContentPropertyNames from "@coremedia/studio-client.cap-rest-client/content/ContentPropertyNames";
import IconButton from "@coremedia/studio-client.ext.ui-components/components/IconButton";
import {bind} from "@jangaroo/runtime";
import CoreIcons_properties from "@coremedia/studio-client.core-icons/CoreIcons_properties";
import Container from "@jangaroo/ext-ts/container/Container";
import HBoxLayout from "@jangaroo/ext-ts/layout/container/HBox";
import BindVisibilityPlugin from "@coremedia/studio-client.ext.ui-components/plugins/BindVisibilityPlugin";
import DisplayField from "@jangaroo/ext-ts/form/field/Display";
import DisplayFieldSkin from "@coremedia/studio-client.ext.ui-components/skins/DisplayFieldSkin";
import MessageBoxUtil from "@coremedia/studio-client.ext.ui-components/messagebox/MessageBoxUtil";
import FormUtils from "../FormUtils";

interface FormEditorFormConfig extends Config<DocumentTabPanel> {
}

class FormEditorForm extends DocumentTabPanel {
  declare Config: FormEditorFormConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.config.formEditorForm";

  static readonly #FORM_ACTIONS: Array<any> = [
    {
      id: "default",
      value: FormEditor_properties.FormEditor_actions_default,
    },
    {
      id: "mailAction",
      value: FormEditor_properties.FormEditor_actions_mail,
    },
  ];

  activatePageableForms(): void {
    let self = this.bindTo;
    this.bindTo.loadValue(function (content: Content): void {
      MessageBoxUtil.showConfirmation(FormEditor_properties.FormEditor_pages_mode_switch_title,
              FormEditor_properties.FormEditor_pages_mode_switch_text_multi, "Ok", buttonId => {
                if (buttonId === "ok") {
                  FormUtils.migrateToMultiPageForm(content, self);
                }
              });
    });
  }

  deActivatePageableForms(): void {

    let self = this.bindTo;
    this.bindTo.loadValue(function (content: Content): void {
      MessageBoxUtil.showConfirmation(FormEditor_properties.FormEditor_pages_mode_switch_title,
              FormEditor_properties.FormEditor_pages_mode_switch_text_single, "Ok", buttonId => {
                if (buttonId === "ok") {
                  FormUtils.migrateToSinglePageForm(content, self);
                }
              });
    });
  }

  showActivateButton(setting: number): boolean {
    return !setting || setting == 0;
  }

  constructor(config: Config<FormEditorForm> = null) {
    super((() => ConfigUtils.apply(Config(FormEditorForm, {

      items: [
        Config(DocumentForm, {
          title: FormEditor_properties.FormEditor_tab_content_title,
          items: [
            Config(DetailsDocumentForm, {bindTo: config.bindTo}),

            Config(TeaserDocumentForm, {
              bindTo: config.bindTo,
              collapsed: true,
            }),
            Config(MediaDocumentForm, {bindTo: config.bindTo}),
            Config(RelatedDocumentForm, {bindTo: config.bindTo}),
            Config(ViewTypeSelectorForm, {bindTo: config.bindTo}),
            Config(ExternallyVisibleDateForm, {bindTo: config.bindTo}),
            Config(ValidityDocumentForm, {bindTo: config.bindTo}),
          ],
        }),
        Config(DocumentForm, {
          title: FormEditor_properties.FormEditor_tab_formData_title,
          items: [

            Config(PropertyFieldGroup, {
              title: FormContentTypes_properties.FormEditor_spamProtectionEnabled_group_text,
              collapsed: false,
              itemId: "spamProtectionGroup",
              items: [
                Config(BooleanPropertyField, {propertyName: "spamProtectionEnabled"}),
              ],
            }),
            Config(PropertyFieldGroup, {
              title: FormContentTypes_properties.FormEditor_pageableFormEnabled_group_text,
              collapsed: false,
              itemId: "pageableFormGroup",
              items: [
                Config(Container, {
                  layout: Config(HBoxLayout),
                  items: [
                    Config(IconButton, {
                      iconCls: CoreIcons_properties.type_object,
                      tooltip: FormEditor_properties.FormEditor_pages_mode_single,
                      handler: bind(this, this.deActivatePageableForms),
                    }),
                    Config(DisplayField, {
                      value: FormEditor_properties.FormEditor_pages_mode_single,
                      ui: DisplayFieldSkin.ITALIC.getSkin(),
                    }),
                  ],
                  ...ConfigUtils.append({
                    plugins: [
                      Config(BindVisibilityPlugin, {
                        bindTo: config.bindTo.extendBy(ContentPropertyNames.PROPERTIES, FormsStudioPlugin.PAGEABLE_ENABLED),
                      }),
                    ]
                  })
                }),
                Config(Container, {
                  layout: Config(HBoxLayout),
                  items: [
                    Config(IconButton, {
                      iconCls: CoreIcons_properties.copy,
                      tooltip: FormEditor_properties.FormEditor_pages_mode_multi,
                      handler: bind(this, this.activatePageableForms),
                    }),
                    Config(DisplayField, {
                      value: FormEditor_properties.FormEditor_pages_mode_multi,
                      ui: DisplayFieldSkin.ITALIC.getSkin(),
                    }),
                  ],
                  ...ConfigUtils.append({
                    plugins: [
                      Config(BindVisibilityPlugin, {
                        bindTo: config.bindTo.extendBy(ContentPropertyNames.PROPERTIES, FormsStudioPlugin.PAGEABLE_ENABLED),
                        transformer: this.showActivateButton
                      }),
                    ]
                  })
                }),
              ],
            }),
            Config(PropertyFieldGroup, {
              title: FormContentTypes_properties.FormEditor_formAction_group_text,
              collapsed: false,
              itemId: "formActionGroup",
              items: [
                Config(LocalComboBox, {
                  itemId: "formActionBox",
                  displayField: "value",
                  width: "100%",
                  valueField: "id",
                  editable: false,
                  ...ConfigUtils.append({
                    plugins: [
                      Config(BindPropertyPlugin, {
                        bindTo: config.bindTo.extendBy("properties", "formAction"),
                        bidirectional: true,
                        componentEvent: "select",
                      }),
                    ],
                  }),
                  store: new JsonStore({
                    fields: ["id", "value"],
                    data: FormEditorForm.#FORM_ACTIONS,
                  }),
                }),
                Config(StringPropertyField, {propertyName: "adminMails"}),
              ],
            }),

          ],
        }),
        Config(FormEditorDocumentForm, {
          bindTo: config.bindTo,
          forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
          structPropertyName: FormsStudioPlugin.FORM_ELEMENTS_STRUCT_PROPERTY,
          formElements: [
            Config(NumberFieldEditor),
            Config(TextAreaEditor),
            Config(TextFieldEditor),
            Config(TextFieldEditor, {
              formElementType: "ZipField",
              defaultRegexpValidatorValue: "\\d{5}",
              formElementIconCls: SvgIconUtil.getIconStyleClassForSvgIcon(InputCity),
              defaultMandatory: true,
              defaultName: FormEditor_properties.FormEditor_label_element_zipField,
            }),
            Config(TextFieldEditor, {
              formElementType: "PhoneField",
              formElementIconCls: SvgIconUtil.getIconStyleClassForSvgIcon(InputPhone),
              defaultName: FormEditor_properties.FormEditor_label_element_phoneField,
            }),
            Config(TextFieldEditor, {
              formElementType: "FaxField",
              formElementIconCls: SvgIconUtil.getIconStyleClassForSvgIcon(InputFax),
              defaultName: FormEditor_properties.FormEditor_label_element_faxField,
            }),
            Config(TextFieldEditor, {
              formElementType: "StreetNumberField",
              formElementIconCls: SvgIconUtil.getIconStyleClassForSvgIcon(InputStreet),
              defaultMandatory: true,
              defaultName: FormEditor_properties.FormEditor_label_element_streetNumberField,
            }),
            Config(TextOnlyEditor),
            Config(UsersMailEditor),
            Config(ConsentFormCheckBoxEditor),
            Config(FileUploadEditor),
            Config(SelectBoxEditor),
            Config(CheckBoxesEditor),
            Config(RadioButtonsEditor),
            Config(DateFieldEditor),
            Config(IbanFieldEditor),
            Config(HiddenFieldEditor),
          ],
        }),
        Config(MultiLanguageDocumentForm, {bindTo: config.bindTo}),
        Config(MetaDataDocumentForm),
        Config(DocumentForm, {
          title: BlueprintTabs_properties.Tab_system_title,
          itemId: "system",
          autoHide: true,
          items: [
            Config(DocumentInfo),
            Config(VersionHistory),
            Config(ReferrerListPanel),
            Config(SearchableForm, {collapsed: true}),
            Config(LinkedSettingsForm, {collapsed: true}),
            Config(LocalSettingsForm, {collapsed: true}),
            Config(PropertyFieldGroup, {
              title: FormContentTypes_properties.FormEditor_formData_text,
              collapsed: true,
              itemId: "formDataForm",

              items: [
                Config(StructPropertyField, {
                  propertyName: FormsStudioPlugin.FORM_ELEMENTS_STRUCT_PROPERTY,
                  hideLabel: true,
                  itemId: "formData",
                }),
              ],

            }),
            Config(DocumentMetaDataFormDispatcher),
          ],

        }),
      ],

    }), config))());
  }
}

export default FormEditorForm;
