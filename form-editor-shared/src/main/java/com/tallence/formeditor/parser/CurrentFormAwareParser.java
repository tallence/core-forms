package com.tallence.formeditor.parser;

import com.coremedia.cap.content.Content;
import com.coremedia.cap.struct.Struct;
import com.tallence.formeditor.elements.FormElement;

/**
 * Can be implemented if the currentForm is required by the elements. E.g. for using the settingsService.
 * The {@link com.tallence.formeditor.FormElementFactory} will handle this case.
 * @param <T>
 */
public interface CurrentFormAwareParser<T extends FormElement<?>> {

  T instantiateType(Content currentForm, Struct elementData);
}
