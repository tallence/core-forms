package com.tallence.formeditor.parser;

import com.coremedia.cap.common.CapStruct;
import com.coremedia.cap.content.Content;
import com.coremedia.cap.struct.Struct;
import com.tallence.formeditor.FormEditorHelper;
import com.tallence.formeditor.FormElementFactory;
import com.tallence.formeditor.elements.PageElement;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Optional;

import static com.coremedia.cap.common.CapStructHelper.getStruct;
import static com.tallence.formeditor.FormEditorHelper.FORM_ELEMENTS;

@Component
public class PageElementParser extends AbstractFormElementParser<PageElement> implements CurrentFormAwareParser<PageElement> {

  public static final String parserKey = "PageElement";

  /**
   * Inject the factory lazy to avoid a circular reference: the factory injects all parser beans.
   */
  @Lazy
  @Inject
  private FormElementFactory formElementFactory;

  @Override
  public PageElement instantiateType(Struct elementData) {
    throw new IllegalStateException("This should not be called, because we need the currentForm");
  }

  @Override
  public PageElement instantiateType(Content currentForm, Struct elementData) {
    return new PageElement(currentForm);
  }

  /**
   * Parse the subElements of the page.
   * @param formElement the current form element which is not filled completely yet.
   * @param elementData the struct representation of the current form field.
   */
  @Override
  public void parseSpecialFields(PageElement formElement, Struct elementData) {

    var formData = Optional.ofNullable(getStruct(elementData, FORM_ELEMENTS))
            .map(CapStruct::getProperties)
            .orElse(Collections.emptyMap());

    var formElements = FormEditorHelper.parseFormElements(formElement.getCurrentForm(), formData, formElementFactory);
    formElement.setSubElements(formElements);

    var pageDescription = elementData.get("pageDescription") != null ? elementData.getMarkup("pageDescription") : null;
    formElement.setPageDescription(pageDescription);

    var pageType = elementData.getString("pageType");
    formElement.setPageType(pageType);

  }

  @Override
  public String getParserKey() {
    return parserKey;
  }
}
