import ValueExpression from "@coremedia/studio-client.client-core/data/ValueExpression";
import BEMPlugin from "@coremedia/studio-client.ext.ui-components/plugins/BEMPlugin";
import Editor_properties from "@coremedia/studio-client.main.editor-components/Editor_properties";
import Container from "@jangaroo/ext-ts/container/Container";
import Label from "@jangaroo/ext-ts/form/Label";
import CenterLayout from "@jangaroo/ext-ts/layout/container/Center";
import HBoxLayout from "@jangaroo/ext-ts/layout/container/HBox";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import FormEditor_properties from "../bundles/FormEditor_properties";
import FormElementsManager from "../helper/FormElementsManager";
import FormElementDropContainerBase from "./FormElementDropContainerBase";

interface FormElementDropContainerConfig extends Config<FormElementDropContainerBase>, Partial<Pick<FormElementDropContainer,
  "formElementsManager" |
  "formElementId" |
  "forceReadOnlyValueExpression"
>> {
}

class FormElementDropContainer extends FormElementDropContainerBase {
  declare Config: FormElementDropContainerConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.config.formElementDropContainer";

  #formElementsManager: FormElementsManager = null;

  get formElementsManager(): FormElementsManager {
    return this.#formElementsManager;
  }

  set formElementsManager(value: FormElementsManager) {
    this.#formElementsManager = value;
  }

  #formElementId: string = null;

  get formElementId(): string {
    return this.#formElementId;
  }

  set formElementId(value: string) {
    this.#formElementId = value;
  }

  #forceReadOnlyValueExpression: ValueExpression = null;

  get forceReadOnlyValueExpression(): ValueExpression {
    return this.#forceReadOnlyValueExpression;
  }

  set forceReadOnlyValueExpression(value: ValueExpression) {
    this.#forceReadOnlyValueExpression = value;
  }

  constructor(config: Config<FormElementDropContainer> = null) {
    super((()=> ConfigUtils.apply(Config(FormElementDropContainer, {

      plugins: [
        Config(BEMPlugin, {
          block: "form-element-drop-container",
          modifier: this.getModifiers(config),
        }),
      ],
      items: [
        Config(Container, {
          items: [
            Config(Container, {
              cls: Editor_properties.LinkListPropertyField_icon,
              width: 16,
              height: 16,
            }),
            Config(Label, { text: FormEditor_properties.FormEditor_text_add_element }),
          ],
          layout: Config(HBoxLayout),
        }),
      ],
      layout: Config(CenterLayout),

    }), config))());
  }
}

export default FormElementDropContainer;
