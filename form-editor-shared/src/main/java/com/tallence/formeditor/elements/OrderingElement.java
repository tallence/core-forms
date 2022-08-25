package com.tallence.formeditor.elements;

import java.util.List;

/**
 * Marker interface for Ordering elements like a PageElement.
 * <br/>
 * Flatten the elements before further handling (validation, serializing, etc.)
 */
public interface OrderingElement {

  List<FormElement<?>> flattenFormElements();
}
