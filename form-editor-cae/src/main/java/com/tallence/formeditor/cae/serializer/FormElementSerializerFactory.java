package com.tallence.formeditor.cae.serializer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.function.BiFunction;

/**
 * Creates an instance of a {@link FormElementSerializerBase} with the given context related objects. Custom
 * dependencies can be added in the implementations, e.g. {@link ConsentFormCheckBoxSerializerFactory}
 * @param <T> the type of the {@link FormElementSerializerBase}
 */
public interface FormElementSerializerFactory<T extends FormElementSerializerBase<?>> {

  T createInstance(BiFunction<String, Object[], String> messageResolver,
                   HttpServletRequest request,
                   HttpServletResponse response);
}
