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

import Config from "@jangaroo/runtime/Config";
import AbstractFormElement from "./AbstractFormElement";

interface ElementGroupBaseConfig extends Config<AbstractFormElement>, Partial<Pick<ElementGroupBase,
        "multipleDefaultValuesAllowed">> {
}

class ElementGroupBase extends AbstractFormElement {
  declare Config: ElementGroupBaseConfig;

  multipleDefaultValuesAllowed: boolean = false;

  constructor(config: Config<ElementGroupBase> = null) {
    super(config);
    this.multipleDefaultValuesAllowed = config.multipleDefaultValuesAllowed;
  }

  /*
  TODO check if multiple defaultValues are set

  if (!multipleDefaultValuesAllowed && defaultValues.length > 0) {
    MessageBoxUtil.showWarn("Mehrere Default-Werte", "Nur für Check-Boxen können mehrere Default-Werte auf einmal verwendet werden. Bitte entfernen Sie zunächst die \"Default-Wert-Markierung\" an den anderen Elementen.");*/

}

export default ElementGroupBase;
