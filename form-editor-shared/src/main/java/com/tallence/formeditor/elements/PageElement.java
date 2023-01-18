package com.tallence.formeditor.elements;

import com.coremedia.xml.Markup;
import com.tallence.formeditor.validator.PageElementValidator;
import com.tallence.formeditor.validator.ValidationFieldError;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Ordering formElement, which is used to structure formFields in Pages. Cannot have a value.
 */
public class PageElement extends AbstractFormElement<List, PageElementValidator> implements OrderingElement {

  private Markup pageDescription;
  private PageType pageType;

  private List<FormElement<?>> subElements;


  public PageElement() {
    super(List.class);
  }



  @Override
  public List<ValidationFieldError> getValidationResult() {
    throw new IllegalStateException("Page elements cannot be validated. Flatten all elements first and skip the page elements.");
  }

  @Override
  public boolean dependencyFulfilled(List<FormElement<?>> allElements) {
    throw new IllegalStateException("Page elements cannot be validated. Flatten all elements first and skip the page elements.");
  }

  @Override
  public void setValue(MultiValueMap<String, String> postData, HttpServletRequest request) {
    throw new IllegalStateException("Page elements cannot be validated. Flatten all elements first and skip the page elements.");
  }

  @Override
  public String serializeValue() {
    throw new IllegalStateException("Page elements cannot be validated. Flatten all elements first and skip the page elements.");
  }

  @Override
  public void fillFormData(Map<String, String> formData) {
    throw new IllegalStateException("Page elements cannot be validated. Flatten all elements first and skip the page elements.");
  }

  public void setSubElements(List<FormElement<?>> subElements) {
    this.subElements = subElements;
  }

  @Override
  public List<FormElement<?>> flattenFormElements() {
    return Optional.ofNullable(subElements).orElse(Collections.emptyList());
  }

  public List<FormElement<?>> getSubElements() {
    return flattenFormElements();
  }

  public void setPageDescription(Markup pageDescription) {
    this.pageDescription = pageDescription;
  }

  public Markup getPageDescription() {
    return pageDescription;
  }

  public PageType getPageType() {
    return pageType;
  }

  public void setPageType(PageType pageType) {
    this.pageType = pageType;
  }

  public void setPageType(String pageType) {
    setPageType(PageType.valueOf(pageType));
  }

  public enum PageType {
    DEFAULT_PAGE, SUMMARY_PAGE
  }
}
