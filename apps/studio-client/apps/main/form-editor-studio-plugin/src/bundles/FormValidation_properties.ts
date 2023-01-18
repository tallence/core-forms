
/**
 * Interface values for ResourceBundle "FormValidation".
 * @see FormValidation_properties#INSTANCE
 */
interface FormValidation_properties {

/**
 *Custom Validator messages
 */
  Validator_form_action_mail_file_text: string;
  Validator_form_action_mail_text: string;
  Validator_consentForm_missing_linkTarget_text: string;
  Validator_consentForm_missing_hint_text: string;
  Validator_consentForm_invalid_hint_text: string;
  Validator_radiobuttongroup_missing_options_text: string;
  Validator_checkboxesgroup_missing_options_text: string;
  Validator_checkboxes_options_min_lower_zero_text: string;
  Validator_checkboxes_options_min_greater_options_text: string;
  Validator_checkboxes_options_max_lower_zero_text: string;
  Validator_checkboxes_options_max_lower_options_text: string;
  Validator_checkboxes_options_max_lower_preselection_text: string;
  Validator_checkboxes_options_max_lower_min_text: string;
  Validator_selectbox_missing_options_text: string;
  Validator_formField_missing_name_text: string;
  Validator_formField_ordering_error_text: string;
  Validator_formField_summaryPage_multiple_error_text: string;
  Validator_formField_summaryPage_middle_error_text: string;
  Validator_formfield_validator_invalid_minsize_text: string;
  Validator_formfield_validator_invalid_maxsize_text: string;
  Validator_formfield_validator_maxsize_smaller_minsize_text: string;
  Validator_formfield_validator_invalid_regexp_text: string;
  Validator_formfield_validator_duplicate_id_text: string;
}

/**
 * Singleton for the current user Locale's instance of ResourceBundle "FormValidation".
 * @see FormValidation_properties
 */
const FormValidation_properties: FormValidation_properties = {
  Validator_form_action_mail_file_text: "The mail action \"mail\" cannot be used with fileUpload-fields.",
  Validator_form_action_mail_text: "The mail action \"mail\" cannot be used without entering a mail address.",
  Validator_consentForm_missing_linkTarget_text: "The ConsentForm '{0}' must link to a document, describing the consent.",
  Validator_consentForm_missing_hint_text: "The ConsentForm '{0}' must contain a Text, describing the consent.",
  Validator_consentForm_invalid_hint_text: "The ConsentForm '{0}' must contain a Text, containing a placeholder like \"%click here for more info%\".",
  Validator_radiobuttongroup_missing_options_text: "The RadioButtons '{0}' must have at least one button.",
  Validator_checkboxesgroup_missing_options_text: "The CheckBoxes '{0}' must have at least one checkBox.",
  Validator_checkboxes_options_min_lower_zero_text: "Please check the field '{0}'. The min. selection should be at least 0.",
  Validator_checkboxes_options_min_greater_options_text: "The number of available options of CheckBoxes '{0}' is lower than the min. required options.",
  Validator_checkboxes_options_max_lower_zero_text: "Please check the field '{0}'. The max. selection should be at least 0.",
  Validator_checkboxes_options_max_lower_options_text: "The number of available options of CheckBoxes '{0}' is lower than the max. allowed options.",
  Validator_checkboxes_options_max_lower_preselection_text: "The number of preselected options of CheckBoxes '{0}' is higher than the max. allowed options.",
  Validator_checkboxes_options_max_lower_min_text: "Please check the field '{0}'. The min. selection should be lower than max. selection.",
  Validator_selectbox_missing_options_text: "The DropDown '{0}' must have at least one option.",
  Validator_formField_missing_name_text: "The FormField of the type '{0}' must have a name, please enter one.",
  Validator_formField_ordering_error_text: "An error occurred in the data structure. Please contact an administrator.",
  Validator_formField_summaryPage_multiple_error_text: "A summary page cannot be used more than once",
  Validator_formField_summaryPage_middle_error_text: "A summary page must be the last page",
  Validator_formfield_validator_invalid_minsize_text: "The validator of the FormField '{0}' has an invalid minSize: '{1}'.",
  Validator_formfield_validator_invalid_maxsize_text: "The validator of the FormField '{0}' has an invalid maxSize: '{1}'.",
  Validator_formfield_validator_maxsize_smaller_minsize_text: "The validator of the FormField '{0}' has a minSize which is greater than the maxSize.",
  Validator_formfield_validator_invalid_regexp_text: "The validator of the FormField '{0}' uses an invalid regular expression to validate the input.",
  Validator_formfield_validator_duplicate_id_text: "Two fields with the same Id '{0}' are not valid! Please remind the customIds (AdvancedSettings).",
};

export default FormValidation_properties;
