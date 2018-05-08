package com.tallence.formeditor.studio.fields {
import com.coremedia.ui.data.ValueExpression;
import com.coremedia.ui.data.ValueHolder;

/**
 * A ValueHolder to wrap the {@link ValueExpression} for a linked content list. If the value expression is undefined,
 * the value holder will return an empty array.
 */
public class SingleLinkValueExpressionHolder implements ValueHolder {

  private var valueExpression:ValueExpression;

  public function SingleLinkValueExpressionHolder(valueExpression:ValueExpression) {
    this.valueExpression = valueExpression;
  }

  public function getValue():* {
    if (valueExpression && valueExpression.getValue() != undefined) {
      return valueExpression.getValue();
    } else {
      return [];
    }
  }

  public function setValue(value:*):Boolean {
    if (valueExpression) {
      return valueExpression.setValue(value);
    }
    return true;
  }
}
}