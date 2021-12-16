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
