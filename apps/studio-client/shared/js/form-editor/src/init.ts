import contentTypeLocalizationRegistry from "@coremedia/studio-client.cap-base-models/content/contentTypeLocalizationRegistry";
import FormEditorDocTypes_properties from "./FormEditorDocTypes_properties";
import typeForm from "./icons/type-form.svg";

contentTypeLocalizationRegistry.addLocalization("FormEditor", {
  displayName: FormEditorDocTypes_properties.FormEditor_displayName,
  svgIcon: typeForm,
  properties: {
    formAction: { displayName: FormEditorDocTypes_properties.FormEditor_formAction_displayName },
    adminMails: {
      displayName: FormEditorDocTypes_properties.FormEditor_adminMails_displayName,
      emptyText: FormEditorDocTypes_properties.FormEditor_adminMails_emptyText,
    },
    formAction_group: { displayName: FormEditorDocTypes_properties.FormEditor_formAction_group_displayName },
    formData: { displayName: FormEditorDocTypes_properties.FormEditor_formData_displayName },
    spamProtectionEnabled: { displayName: FormEditorDocTypes_properties.FormEditor_spamProtectionEnabled_displayName },
    spamProtectionEnabled_true: { displayName: FormEditorDocTypes_properties.FormEditor_spamProtectionEnabled_true_displayName },
    spamProtectionEnabled_group: { displayName: FormEditorDocTypes_properties.FormEditor_spamProtectionEnabled_group_displayName },
  },
});
