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

import CollapsiblePanel from "@coremedia/studio-client.ext.ui-components/components/panel/CollapsiblePanel";
import HighlightableMixin from "@coremedia/studio-client.ext.ui-components/mixins/HighlightableMixin";
import ValidationStateMixin from "@coremedia/studio-client.ext.ui-components/mixins/ValidationStateMixin";
import Events from "@jangaroo/ext-ts/Events";
import { mixin } from "@jangaroo/runtime";
import Config from "@jangaroo/runtime/Config";
import StateFulCollapsiblePanel from "./StateFulCollapsiblePanel";

interface StateFulCollapsiblePanelBaseConfig extends Config<CollapsiblePanel>, Config<ValidationStateMixin>, Config<HighlightableMixin> {
  listeners?: Events<CollapsiblePanel> & Events<ValidationStateMixin> & Events<HighlightableMixin>;
}

/**
 * This component extends the {@link CollapsiblePanel} and implements the {@link IValidationStateMixin} and
 * {@link IHighlightableMixin} interface to display a validation state.
 */
class StateFulCollapsiblePanelBase extends CollapsiblePanel {
  declare Config: StateFulCollapsiblePanelBaseConfig;

  constructor(config: Config<StateFulCollapsiblePanel> = null) {
    super(config);
    this.initValidationStateMixin();
  }
}

interface StateFulCollapsiblePanelBase extends ValidationStateMixin, HighlightableMixin{}

mixin(StateFulCollapsiblePanelBase, ValidationStateMixin, HighlightableMixin);

export default StateFulCollapsiblePanelBase;
