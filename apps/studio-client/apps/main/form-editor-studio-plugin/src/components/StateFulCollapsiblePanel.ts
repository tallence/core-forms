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
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import FormEditor_properties from "../bundles/FormEditor_properties";
import StateFulCollapsiblePanelBase from "./StateFulCollapsiblePanelBase";

interface StateFulCollapsiblePanelConfig extends Config<StateFulCollapsiblePanelBase>, Partial<Pick<StateFulCollapsiblePanel,
  "ariaLabelOverride"
>> {
}

class StateFulCollapsiblePanel extends StateFulCollapsiblePanelBase {
  declare Config: StateFulCollapsiblePanelConfig;

  constructor(config: Config<StateFulCollapsiblePanel> = null) {
    super(ConfigUtils.apply(Config(StateFulCollapsiblePanel, { ariaLabel: ConfigUtils.asString(config.ariaLabelOverride ? config.ariaLabelOverride : FormEditor_properties.FormEditor_element_collapsiblePanel_label) }), config));
  }

  #ariaLabelOverride: string = null;

  get ariaLabelOverride(): string {
    return this.#ariaLabelOverride;
  }

  set ariaLabelOverride(value: string) {
    this.#ariaLabelOverride = value;
  }
}

export default StateFulCollapsiblePanel;
