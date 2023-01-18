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

import { bind } from "@jangaroo/runtime";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import ShowFormIssuesPluginBase from "./ShowFormIssuesPluginBase";

interface ShowFormIssuesPluginConfig extends Config<ShowFormIssuesPluginBase> {
}

class ShowFormIssuesPlugin extends ShowFormIssuesPluginBase {
  declare Config: ShowFormIssuesPluginConfig;

  static readonly xtype: string = "com.tallence.formeditor.studio.config.showFormIssuesPlugin";

  constructor(config: Config<ShowFormIssuesPlugin> = null) {
    super((()=> ConfigUtils.apply(Config(ShowFormIssuesPlugin, {
      bindTo: this.getIssuesVE(config),
      boundValueChanged: bind(this, this.issuesChanged),

    }), config))());
  }
}

export default ShowFormIssuesPlugin;
