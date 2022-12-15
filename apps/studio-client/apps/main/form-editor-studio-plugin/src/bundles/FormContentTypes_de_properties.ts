import ResourceBundleUtil from "@jangaroo/runtime/l10n/ResourceBundleUtil";
import FormContentTypes_properties from "./FormContentTypes_properties";

/**
 * Overrides of ResourceBundle "FormContentTypes" for Locale "de".
 * @see FormContentTypes_properties#INSTANCE
 */
ResourceBundleUtil.override(FormContentTypes_properties, {
  FormEditor_text: "Formular",
  FormEditor_formAction_text: "Formular-Ziel",
  FormEditor_adminMails_text: "Wird per E-Mail informiert:",
  FormEditor_adminMails_emptyText: "Mehrere Adressen können per Komma getrennt werden",
  FormEditor_formAction_group_text: "Formular-Ziel",
  FormEditor_formData_text: "Formular-Einstellungen",
  FormEditor_spamProtectionEnabled_text: "Spam-Schutz aktivieren",
  FormEditor_spamProtectionEnabled_true_text: "Spam-Schutz aktivieren",
  FormEditor_spamProtectionEnabled_group_text: "Spam-Schutz",
  FormEditor_pageableFormEnabled_group_text: "Mehrseitigkeit",
  "FormEditor_formData.formElements.{formElementId:[0-9]+}.name_text": "Name",
  "FormEditor_formData.formElements.{formElementId:[0-9]+}.hint_text": "Hinweis",
  "FormEditor_formData.formElements.{formElementId:[0-9]+}.linkTarget_text": "Einverständniserklärung",
  "FormEditor_formData.formElements.{formElementId:[0-9]+}.groupElements_text": "Check-, Radio-Boxes",
  "FormEditor_formData.formElements.{formElementId:[0-9]+}.validator.maxSize_text": "Max. Zeichen",
  "FormEditor_formData.formElements.{formElementId:[0-9]+}.validator.minSize_text": "Min. Zeichen",
});
