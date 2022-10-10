import ResourceBundleUtil from "@jangaroo/runtime/l10n/ResourceBundleUtil";
import FormValidation_properties from "./FormValidation_properties";

/**
 * Overrides of ResourceBundle "FormValidation" for Locale "de".
 * @see FormValidation_properties#INSTANCE
 */
ResourceBundleUtil.override(FormValidation_properties, {
  Validator_form_action_mail_file_text: "Der Formular-Typ Mail kann nicht mit einem Formularelement vom Typ \"Dateiupload\" verwendet werden.",
  Validator_form_action_mail_text: "Bitte geben Sie eine E-Mail-Adresse ein wenn der Formular-Typ \"Mail\" genutzt wird.",
  Validator_consentForm_missing_linkTarget_text: "Die Einverständniserklärung '{0}' muss einen Link zu einem Dokument mit mehr Informationen enthalten.",
  Validator_consentForm_missing_hint_text: "Die Einverständniserklärung '{0}' muss einen Text enthalten.",
  Validator_consentForm_invalid_hint_text: "Die Einverständniserklärung '{0}' muss einen Text mit einem Platzhalter, z.B. \"bitte die %Erklärung% bestätigen\".",
  Validator_radiobuttongroup_missing_options_text: "Die RadioButtons '{0}' müssen mindestens einen Button haben.",
  Validator_checkboxesgroup_missing_options_text: "Die CheckBoxen '{0}' müssen mindestens eine CheckBox haben.",
  Validator_checkboxes_options_min_lower_zero_text: "Die 'Min. Auswahl' der CheckBoxen '{0}' darf nicht kleiner als 0 sein.",
  Validator_checkboxes_options_min_greater_options_text: "Die festgelegte 'Min. Auswahl' übersteigt die Anzahl der verfügbaren Optionen von CheckBoxen '{0}'.",
  Validator_checkboxes_options_max_lower_zero_text: "Die 'Max. Auswahl' der CheckBoxen '{0}' darf nicht kleiner als 0 sein.",
  Validator_checkboxes_options_max_lower_options_text: "Die festgelegte 'Max. Auswahl' übersteigt die Anzahl der verfügbaren Optionen von CheckBoxen '{0}'.",
  Validator_checkboxes_options_max_lower_preselection_text: "Die festgelegte 'Max. Auswahl' übersteigt die Anzahl der vorausgewählten Optionen von CheckBoxen '{0}'.",
  Validator_checkboxes_options_max_lower_min_text: "Bei den Checkboxen '{0}' darf die 'Min. Auswahl' nicht größer als die 'Max. Auswahl' sein.",
  Validator_selectbox_missing_options_text: "Die DropDown-Liste '{0}' muss mindestens eine Option haben.",
  Validator_formField_missing_name_text: "Das Formular-Element vom Typ '{0}' muss einen Namen haben.",
  Validator_formfield_validator_invalid_minsize_text: "Der Validator des Formular-Elements '{0}' hat eine invalide minimale Größe: '{1}'.",
  Validator_formfield_validator_invalid_maxsize_text: "Der Validator des Formular-Elements '{0}' hat eine invalide maximale Größe: '{1}'.",
  Validator_formfield_validator_maxsize_smaller_minsize_text: "Der Validator des Formular-Elements '{0}' nutzt eine minimale Größe, die größer als die maximale Größe ist.",
  Validator_formfield_validator_invalid_regexp_text: "Der Validator des Formular-Elements '{0}' nutzt einen invaliden regulären Ausdruck.",
  Validator_formfield_validator_duplicate_id_text: "Zwei Felder mit der selben Id '{0}' sind nicht gültig! Bitte beachten Sie auch die Custom-Id (Erweiterte Einstellungen).",
});
