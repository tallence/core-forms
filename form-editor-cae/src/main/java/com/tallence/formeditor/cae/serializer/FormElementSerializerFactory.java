package com.tallence.formeditor.cae.serializer;

import java.util.function.BiFunction;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
