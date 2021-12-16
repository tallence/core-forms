import ValueExpression from "@coremedia/studio-client.client-core/data/ValueExpression";
import BindComponentsPlugin from "@coremedia/studio-client.ext.ui-components/plugins/BindComponentsPlugin";
import Container from "@jangaroo/ext-ts/container/Container";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import AppliedFormElementContainer from "./AppliedFormElementContainer";
import FormElementDropContainer from "./dragdrop/FormElementDropContainer";
import FormElementsManager from "./helper/FormElementsManager";

interface AppliedElementsContainerConfig extends Config<Container>, Partial<Pick<AppliedElementsContainer,
  "bindTo" |
  "forceReadOnlyValueExpression" |
  "formElementsManager"
>> {
}

class AppliedElementsContainer extends Container {
  declare Config: AppliedElementsContainerConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.config.appliedElementsContainer";

  #bindTo: ValueExpression = null;

  /*
     * Content bean value expression
     */
  get bindTo(): ValueExpression {
    return this.#bindTo;
  }

  set bindTo(value: ValueExpression) {
    this.#bindTo = value;
  }

  #forceReadOnlyValueExpression: ValueExpression = null;

  /*
     * An optional ValueExpression which makes the component read-only if it is evaluated to true.
     */
  get forceReadOnlyValueExpression(): ValueExpression {
    return this.#forceReadOnlyValueExpression;
  }

  set forceReadOnlyValueExpression(value: ValueExpression) {
    this.#forceReadOnlyValueExpression = value;
  }

  #formElementsManager: FormElementsManager = null;

  /**
   * The form elements manager contains the value expression evaluating to the applied form elements. The value
   * expression that controls whether a drag/drop operation is active can also be accessed via this manager.
   */
  get formElementsManager(): FormElementsManager {
    return this.#formElementsManager;
  }

  set formElementsManager(value: FormElementsManager) {
    this.#formElementsManager = value;
  }

  constructor(config: Config<AppliedElementsContainer> = null) {
    super(ConfigUtils.apply(Config(AppliedElementsContainer, {

      items: [
        Config(FormElementDropContainer, {
          formElementsManager: config.formElementsManager,
          forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
        }),
        Config(Container, {
          plugins: [
            Config(BindComponentsPlugin, {
              configBeanParameterName: "formElement",
              valueExpression: config.formElementsManager.getFormElementsVE(),
              template: Config(AppliedFormElementContainer, {
                bindTo: config.bindTo,
                forceReadOnlyValueExpression: config.forceReadOnlyValueExpression,
                formElementsManager: config.formElementsManager,
              }),
            }),
          ],
        }),
      ],

    }), config));
  }
}

export default AppliedElementsContainer;
