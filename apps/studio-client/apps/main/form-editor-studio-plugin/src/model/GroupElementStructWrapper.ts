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
import { AnyFunction } from "@jangaroo/runtime/types";

class GroupElementStructWrapper {

  static readonly PROP_CHECKED: string = "checkedByDefault";

  static readonly #PROP_VALUE: string = "value";

  #groupElementVE: ValueExpression = null;

  #id: string = null;

  constructor(groupElementVE: ValueExpression, name: string, onSelectionChangeCallback: AnyFunction) {
    this.#groupElementVE = groupElementVE;
    this.#id = name;

    this.getOptionSelectedByDefaultVE().addChangeListener((): void => {
      if (onSelectionChangeCallback) {
        onSelectionChangeCallback.call(NaN, name, this.getOptionSelectedByDefaultVE().getValue());
      }
    });
  }

  getId(): string {
    return this.#id;
  }

  getGroupElementVE(): ValueExpression {
    return this.#groupElementVE;
  }

  getOptionSelectedByDefaultVE(): ValueExpression {
    return this.getGroupElementVE().extendBy(GroupElementStructWrapper.PROP_CHECKED);
  }

  getOptionValueVE(): ValueExpression {
    return this.getGroupElementVE().extendBy(GroupElementStructWrapper.#PROP_VALUE);
  }

}

export default GroupElementStructWrapper;
