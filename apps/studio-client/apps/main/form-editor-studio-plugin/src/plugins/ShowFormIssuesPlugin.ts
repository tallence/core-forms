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
