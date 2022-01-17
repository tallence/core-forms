import ValueExpression from "@coremedia/studio-client.client-core/data/ValueExpression";
import Container from "@jangaroo/ext-ts/container/Container";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import ApplicableElementsBase from "./ApplicableElementsBase";

interface ApplicableElementsConfig extends Config<ApplicableElementsBase>, Partial<Pick<ApplicableElements,
  "formElements" |
  "dragActiveVE" |
  "readOnlyVE"
>> {
}

class ApplicableElements extends ApplicableElementsBase {
  declare Config: ApplicableElementsConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.config.applicableElements";

  #formElements: Array<any> = null;

  /*
     * Array of objects describing groups of draggable items
     */
  get formElements(): Array<any> {
    return this.#formElements;
  }

  set formElements(value: Array<any>) {
    this.#formElements = value;
  }

  #dragActiveVE: ValueExpression = null;

  /**
   * Stores the information whether a drag and drop operation is in progress.
   */
  get dragActiveVE(): ValueExpression {
    return this.#dragActiveVE;
  }

  set dragActiveVE(value: ValueExpression) {
    this.#dragActiveVE = value;
  }

  #readOnlyVE: ValueExpression = null;

  get readOnlyVE(): ValueExpression {
    return this.#readOnlyVE;
  }

  set readOnlyVE(value: ValueExpression) {
    this.#readOnlyVE = value;
  }

  constructor(config: Config<ApplicableElements> = null) {
    super(ConfigUtils.apply(Config(ApplicableElements, {

      items: [
        Config(Container, { itemId: ApplicableElementsBase.APPLICABLE_ELEMENTS_CONTAINER_ID }),
      ],

    }), config));
  }
}

export default ApplicableElements;
