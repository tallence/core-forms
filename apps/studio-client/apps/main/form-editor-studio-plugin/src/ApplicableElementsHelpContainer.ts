import OpenReferenceWindowAction from "@coremedia/studio-client.main.editor-components/sdk/actions/OpenReferenceWindowAction";
import CollapsiblePanel from "@coremedia/studio-client.main.editor-components/sdk/premular/CollapsiblePanel";
import Button from "@jangaroo/ext-ts/button/Button";
import Window from "@jangaroo/ext-ts/window/Window";
import Config from "@jangaroo/runtime/Config";
import ConfigUtils from "@jangaroo/runtime/ConfigUtils";
import FormEditor_properties from "./bundles/FormEditor_properties";

interface ApplicableElementsHelpContainerConfig extends Config<CollapsiblePanel>, Partial<Pick<ApplicableElementsHelpContainer,
  "helpTextUrl"
>> {
}

class ApplicableElementsHelpContainer extends CollapsiblePanel {
  declare Config: ApplicableElementsHelpContainerConfig;

  static override readonly xtype: string = "com.tallence.formeditor.studio.config.applicableElementsHelpContainer";

  #helpTextUrl: string = null;

  get helpTextUrl(): string {
    return this.#helpTextUrl;
  }

  set helpTextUrl(value: string) {
    this.#helpTextUrl = value;
  }

  constructor(config: Config<ApplicableElementsHelpContainer> = null) {
    super(ConfigUtils.apply(Config(ApplicableElementsHelpContainer, {
      margin: "10 0 0 0",
      title: FormEditor_properties.FormEditor_label_show_help,

      items: [
        Config(Button, {
          itemId: "conditionHelp",
          iconCls: "flat-help-btn",
          text: FormEditor_properties.FormEditor_label_show_help,
          baseAction: new OpenReferenceWindowAction({
            dialog: Config(Window, {
              title: FormEditor_properties.FormEditor_help_title,
              cls: "helper-window",
              width: 530,
              minWidth: 300,
              height: 300,
              minHeight: 265,
              layout: "fit",
              autoScroll: true,
              collapsible: false,
              border: false,
              closeAction: "close",
              constrain: true,
              constrainHeader: true,
              plain: true,
              loader: {
                url: config.helpTextUrl,
                autoLoad: true,
              },
            }),
          }),
        }),
      ],

    }), config));
  }
}

export default ApplicableElementsHelpContainer;
