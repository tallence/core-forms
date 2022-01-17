import ValueExpression from "@coremedia/studio-client.client-core/data/ValueExpression";
import ValueHolder from "@coremedia/studio-client.client-core/data/ValueHolder";
import { mixin } from "@jangaroo/runtime";

/**
 * A ValueHolder to wrap the {@link ValueExpression} for a linked content list. If the value expression is undefined,
 * the value holder will return an empty array.
 */
class SingleLinkValueExpressionHolder implements ValueHolder {

  #valueExpression: ValueExpression = null;

  constructor(valueExpression: ValueExpression) {
    this.#valueExpression = valueExpression;
  }

  getValue(): any {
    if (this.#valueExpression && this.#valueExpression.getValue() != undefined) {
      return this.#valueExpression.getValue();
    } else {
      return [];
    }
  }

  setValue(value: any): boolean {
    if (this.#valueExpression) {
      return this.#valueExpression.setValue(value);
    }
    return true;
  }
}
mixin(SingleLinkValueExpressionHolder, ValueHolder);

export default SingleLinkValueExpressionHolder;
