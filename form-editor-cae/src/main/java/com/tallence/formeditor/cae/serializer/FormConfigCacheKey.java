/*
 * Copyright 2020 Tallence AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tallence.formeditor.cae.serializer;

import com.coremedia.cache.Cache;
import com.coremedia.cache.CacheKey;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.tallence.formeditor.elements.AbstractFormElement;
import com.tallence.formeditor.cae.model.FormEditorConfig;
import com.tallence.formeditor.contentbeans.FormEditor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * Building a string, representing the form config for a formEditor document.
 * <p>
 * Equals and hashcode use the formEditor ContentBean {@link #editor}, which is a unique identifier for the form.
 */
public class FormConfigCacheKey extends CacheKey<String> {

  public static final String FORM_CONFIG_CACHE_KEY = "com.tallence.formeditor.cae.serializer.FormConfigCacheKey";

  private final FormEditor editor;
  private final HttpServletRequest request;
  private final HttpServletResponse response;
  private final BiFunction<String, Object[], String> messageResolver;
  private final FormEditorConfig formEditorConfig;
  private final List<FormElementSerializerFactory<?>> formElementSerializerFactories;


  public FormConfigCacheKey(FormEditor editor,
                            HttpServletRequest request,
                            HttpServletResponse response,
                            BiFunction<String, Object[], String> messageResolver,
                            FormEditorConfig formEditorConfig,
                            List<FormElementSerializerFactory<?>> formElementSerializerFactories) {
    this.editor = editor;
    this.request = request;
    this.response = response;
    this.messageResolver = messageResolver;
    this.formEditorConfig = formEditorConfig;
    this.formElementSerializerFactories = formElementSerializerFactories;
  }

  @Override
  public String evaluate(Cache cache) throws Exception {
    //Disables the current cache entry, if the document changes: call the struct to add the required dependency
    editor.getFormElements();

    SimpleModule module = new SimpleModule();

    addSerializers(module);

    return new ObjectMapper().registerModule(module)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
            .writer(new DefaultPrettyPrinter())
            .writeValueAsString(formEditorConfig);
  }

  /**
   * Enrich the Serializers with context specific helpers and handlers and add them to the jackson module.
   */
  private void addSerializers(SimpleModule module) {

    Stream.concat(Stream.of(new FormElementSerializerBaseFactory()), formElementSerializerFactories.stream())
            .map(f -> f.createInstance(messageResolver, request, response))
            //cast to an untyped Class to satisfy typed method interface. TODO find a better way
            .forEach(s -> module.addSerializer((Class) s.handledType(), s));
  }

  @Override
  public String cacheClass(Cache cache, String value) {
    return FORM_CONFIG_CACHE_KEY;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof FormConfigCacheKey)) return false;
    FormConfigCacheKey that = (FormConfigCacheKey) o;
    return editor.equals(that.editor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(editor);
  }

  private static class FormElementSerializerBaseFactory implements FormElementSerializerFactory<FormElementSerializerBase<AbstractFormElement<?, ?>>> {

    @Override
    public FormElementSerializerBase<AbstractFormElement<?, ?>> createInstance(BiFunction<String, Object[], String> messageResolver,
                                                                               HttpServletRequest request,
                                                                               HttpServletResponse response) {
      return new FormElementSerializerBase(AbstractFormElement.class, messageResolver);
    }
  }
}
