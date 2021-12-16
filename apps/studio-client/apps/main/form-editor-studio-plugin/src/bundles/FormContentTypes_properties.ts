
/**
 * Interface values for ResourceBundle "FormContentTypes".
 * @see FormContentTypes_properties#INSTANCE
 */
interface FormContentTypes_properties {

/**
 *FormEditor
 */
  FormEditor_text: string;
  FormEditor_formAction_text: string;
  FormEditor_adminMails_text: string;
  FormEditor_adminMails_emptyText: string;
  FormEditor_formAction_group_text: string;
  FormEditor_formData_text: string;
  FormEditor_spamProtectionEnabled_text: string;
  FormEditor_spamProtectionEnabled_true_text: string;
  FormEditor_spamProtectionEnabled_group_text: string;
  "FormEditor_formData.formElements.{formElementId:[0-9]+}.name_text": string;
  "FormEditor_formData.formElements.{formElementId:[0-9]+}.hint_text": string;
  "FormEditor_formData.formElements.{formElementId:[0-9]+}.linkTarget_text": string;
  "FormEditor_formData.formElements.{formElementId:[0-9]+}.groupElements_text": string;
  "FormEditor_formData.formElements.{formElementId:[0-9]+}.validator.maxSize_text": string;
  "FormEditor_formData.formElements.{formElementId:[0-9]+}.validator.minSize_text": string;
}

/**
 * Singleton for the current user Locale's instance of ResourceBundle "FormContentTypes".
 * @see FormContentTypes_properties
 */
const FormContentTypes_properties: FormContentTypes_properties = {
  FormEditor_text: "Form",
  FormEditor_formAction_text: "Form-Action Type",
  FormEditor_adminMails_text: "Will be informed via E-Mail",
  FormEditor_adminMails_emptyText: "Multiple addresses can be separated by comma",
  FormEditor_formAction_group_text: "Form-Processing",
  FormEditor_formData_text: "Form-Settings",
  FormEditor_spamProtectionEnabled_text: "activate spam protection",
  FormEditor_spamProtectionEnabled_true_text: "activate spam protection",
  FormEditor_spamProtectionEnabled_group_text: "Spam protection",
  "FormEditor_formData.formElements.{formElementId:[0-9]+}.name_text": "Name",
  "FormEditor_formData.formElements.{formElementId:[0-9]+}.hint_text": "Hint",
  "FormEditor_formData.formElements.{formElementId:[0-9]+}.linkTarget_text": "Consent Form",
  "FormEditor_formData.formElements.{formElementId:[0-9]+}.groupElements_text": "Check-, Radio-Boxes",
  "FormEditor_formData.formElements.{formElementId:[0-9]+}.validator.maxSize_text": "Max. Characters",
  "FormEditor_formData.formElements.{formElementId:[0-9]+}.validator.minSize_text": "Min. Characters",
};

export default FormContentTypes_properties;
