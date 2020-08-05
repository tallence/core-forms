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

import com.coremedia.blueprint.common.contentbeans.CMTeasable;
import com.coremedia.cache.Cache;
import com.coremedia.cache.CacheKey;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.tallence.formeditor.cae.elements.*;
import com.tallence.formeditor.cae.model.FormEditorConfig;
import com.tallence.formeditor.contentbeans.FormEditor;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Building a string, representing the form config for a formEditor document.
 * <p>
 * Equals and hashcode use the field {@link #editor}, which is a unique identifier for the form.
 */
public class FormConfigCacheKey extends CacheKey<String> {

  public static final String FORM_CONFIG_CACHE_KEY = "com.tallence.formeditor.cae.serializer.FormConfigCacheKey";

  private final FormEditor editor;
  private final Function<CMTeasable, String> linkBuilder;
  private final BiFunction<String, Object[], String> messageResolver;
  private final FormEditorConfig formEditorConfig;
  private final ValidationSerializationHelper validationSerializationHelper;


  public FormConfigCacheKey(FormEditor editor,
                            Function<CMTeasable, String> linkBuilder,
                            BiFunction<String, Object[], String> messageResolver,
                            ValidationSerializationHelper validationSerializationHelper,
                            FormEditorConfig formEditorConfig) {
    this.editor = editor;
    this.linkBuilder = linkBuilder;
    this.messageResolver = messageResolver;
    this.formEditorConfig = formEditorConfig;
    this.validationSerializationHelper = validationSerializationHelper;
  }

  @Override
  public String evaluate(Cache cache) throws Exception {
    //Disables the current cache entry, if the document changes: call the struct to add the required dependency
    editor.getFormElements();

    SimpleModule module = new SimpleModule();
    module.addSerializer(AbstractFormElement.class, new AbstractFormElementSerializer(AbstractFormElement.class, messageResolver, validationSerializationHelper));
    module.addSerializer(ConsentFormCheckBox.class, new ConsentFormCheckBoxSerializer(messageResolver, validationSerializationHelper, linkBuilder));
    module.addSerializer(UsersMail.class, new UsersMailSerializer(messageResolver, validationSerializationHelper));
    module.addSerializer(TextArea.class, new TextAreaSerializer(messageResolver, validationSerializationHelper));
    module.addSerializer(TextOnly.class, new TextOnlySerializer(messageResolver, validationSerializationHelper));
    return new ObjectMapper().registerModule(module)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
            .writer(new DefaultPrettyPrinter())
            .writeValueAsString(formEditorConfig);
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
}
