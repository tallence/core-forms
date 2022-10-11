package com.tallence.formeditor.studio.validator.field;

import com.coremedia.cap.content.Content;
import com.coremedia.rest.validation.Issues;
import com.coremedia.rest.validation.Severity;
import com.tallence.formeditor.elements.AdvancedSettings;
import com.tallence.formeditor.elements.FormElement;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.tallence.formeditor.FormEditorHelper.FORM_DATA;

/**
 * Making sure, there are no formElements with duplicate IDs or customIDs.
 */
@Component
public class DuplicateIdValidator implements ComplexValidator {

  public static final String FORMFIELD_VALIDATOR_DUPLICATE_ID_ERROR_CODE = "formfield_validator_duplicate_id";

  @Override
  public void validateFieldIfResponsible(List<FormElement<?>> formElements, String action, Issues issues, Content document) {

    var ids = formElements.stream()
            .map(this::getEffectiveId)
            .collect(Collectors.toList());

    ids.stream().filter(i -> Collections.frequency(ids, i) > 1).findFirst()
            .ifPresent(duplicateId -> issues.addIssue(Severity.ERROR, FORM_DATA, FORMFIELD_VALIDATOR_DUPLICATE_ID_ERROR_CODE, duplicateId));
  }

  private String getEffectiveId(FormElement<?> formElement) {
    return Optional.ofNullable(formElement.getAdvancedSettings())
            .map(AdvancedSettings::getCustomId)
            .filter(StringUtils::hasLength)
            .orElse(formElement.getId());
  }
}
