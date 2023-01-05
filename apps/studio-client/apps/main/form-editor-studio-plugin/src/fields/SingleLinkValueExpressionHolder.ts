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
