package com.tallence.formeditor.validator;

import java.util.List;

public class PageElementValidator implements Validator<List> {

  @Override
  public List<ValidationFieldError> validate(List value) {
    throw new IllegalStateException("Page elements cannot be validated. Flatten all elements first and skip the page elements.");
  }

  @Override
  public boolean isMandatory() {
    return false;
  }
}
