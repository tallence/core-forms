import PropertyEditorUtil from "@coremedia/studio-client.main.editor-components/sdk/util/PropertyEditorUtil";
import Ext from "@jangaroo/ext-ts";
import Container from "@jangaroo/ext-ts/container/Container";
import HBoxLayout from "@jangaroo/ext-ts/layout/container/HBox";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import ApplicableElements from "./ApplicableElements";
import ApplicableElementsHelpContainer from "./ApplicableElementsHelpContainer";
import FormEditorDocumentFormBase from "./FormEditorDocumentFormBase";
import FormEditor_properties from "./bundles/FormEditor_properties";
import AppliedElementsContainer from "./AppliedElementsContainer";
import SwitchingContainer from "@coremedia/studio-client.ext.ui-components/components/SwitchingContainer";
import ValueExpressionFactory from "@coremedia/studio-client.client-core/data/ValueExpressionFactory";
import PagesWrapperContainer from "./pages/PagesWrapperContainer";
import ContentPropertyNames from "@coremedia/studio-client.cap-rest-client/content/ContentPropertyNames";
import FormsStudioPlugin from "./FormsStudioPlugin";

interface FormEditorDocumentFormConfig extends Config<FormEditorDocumentFormBase>, Partial<Pick<FormEditorDocumentForm,
        "formElements" |
        "structPropertyName">> {
}

class FormEditorDocumentForm extends FormEditorDocumentFormBase {
  declare Config: FormEditorDocumentFormConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.config.formEditor";

  static readonly APPLIED_FORM_ELEMENTS: string = "appliedFormElements";
  static readonly APPLIED_FORM_PAGES: string = "appliedFormPages";

  #formElements: Array<any> = null;

  /*
     * Array of Form editor configuration objects for all applicable Form element editors.
     */
  get formElements(): Array<any> {
    return this.#formElements;
  }

  set formElements(value: Array<any>) {
    this.#formElements = value;
  }

  #structPropertyName: string = null;

  /*
     * A String which holds the document property name of the struct, where the form Elements are saved into
     */
  get structPropertyName(): string {
    return this.#structPropertyName;
  }

  set structPropertyName(value: string) {
    this.#structPropertyName = value;
  }

  // called by generated constructor code
  #__initialize__(config: Config<FormEditorDocumentForm>): void {
    this.initReusableComponents(config.formElements);
  }

  getActiveAppliedContainer(pageableFeatureEnabled: Boolean): string {
    return pageableFeatureEnabled ? FormEditorDocumentForm.APPLIED_FORM_PAGES : FormEditorDocumentForm.APPLIED_FORM_ELEMENTS;
  }

  constructor(config: Config<FormEditorDocumentForm> = null) {
    super((() => {
      this.#__initialize__(config);
      return ConfigUtils.apply(Config(FormEditorDocumentForm, {
        title: FormEditor_properties.FormEditor_tab_formFields_title,
        layout: "card",
        region: "center",

        items: [
          Config(Container, {
            items: [
              /* left column, applicable form elements */
              Config(Container, {
                width: 160,
                items: [
                  /* Create a value expression to bind the disabled state of the drag source. It is necessary to use the
            two value expressions 'bindTo' and 'forceReadOnlyValueExpression' to create the read only value expression.
            If a content is checked out by another user, the read only value is not stored in the forceReadOnlyValueExpression. */
                  Config(ApplicableElements, {
                    formElements: config.formElements,
                    dragActiveVE: this.getFormElementsManager(config.bindTo, config.forceReadOnlyValueExpression, config.structPropertyName).getDragActiveVE(),
                    readOnlyVE: PropertyEditorUtil.createReadOnlyValueExpression(config.bindTo, config.forceReadOnlyValueExpression),
                  }),
                  Config(ApplicableElementsHelpContainer, {helpTextUrl: ConfigUtils.asString(Ext.manifest.globalResources[FormEditor_properties.FormEditor_window_html_content_key])}),
                ],
              }),
              /* right column, applied form elements */
              Config(Container, {
                flex: 1,
                layout: "anchor",
                autoScroll: true,
                items: [
                  Config(SwitchingContainer, {
                    activeItemValueExpression: ValueExpressionFactory.createTransformingValueExpression(config.bindTo.extendBy(ContentPropertyNames.PROPERTIES, FormsStudioPlugin.PAGEABLE_ENABLED), this.getActiveAppliedContainer),
                    items: [
                      /* applied form pages */
                      Config(AppliedElementsContainer, {
                        bindTo: config.bindTo,
                        itemId: FormEditorDocumentForm.APPLIED_FORM_ELEMENTS,
                        forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
                        formElementsManager: this.getFormElementsManager(config.bindTo, config.forceReadOnlyValueExpression, config.structPropertyName),
                      }),
                      /* applied form elements */
                      Config(PagesWrapperContainer, {
                        itemId: FormEditorDocumentForm.APPLIED_FORM_PAGES,
                        bindTo: config.bindTo,
                        forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
                        formElementsManager: this.getFormElementsManager(config.bindTo, config.forceReadOnlyValueExpression, config.structPropertyName),
                      })
                    ],
                  }),
                ],
              }),
              /* end right column */
            ],
            /*Hbox layout for the main content beneath the header */
            layout: Config(HBoxLayout, {align: "stretch"}),
          }),
        ],

      }), config);
    })());
  }
}

export default FormEditorDocumentForm;
