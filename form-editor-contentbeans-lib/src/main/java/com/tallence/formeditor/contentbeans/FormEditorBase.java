/*
 * Copyright 2018 Tallence AG
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

package com.tallence.formeditor.contentbeans;

import com.coremedia.blueprint.cae.contentbeans.CMTeasableImpl;
import com.coremedia.cae.aspect.Aspect;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Generated base class for immutable beans of document type FormEditor.
 * Should not be changed.
 */
public abstract class FormEditorBase extends CMTeasableImpl implements FormEditor {

  @Override
  public FormEditor getMaster() {
    return (FormEditor) super.getMaster();
  }

  @Override
  public Map<Locale, ? extends FormEditor> getVariantsByLocale() {
    return getVariantsByLocale(FormEditor.class);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Collection<? extends FormEditor> getLocalizations() {
    return (Collection<? extends FormEditor>) super.getLocalizations();
  }

  @Override
  @SuppressWarnings("unchecked")
  public Map<String, ? extends Aspect<? extends FormEditor>> getAspectByName() {
    return (Map<String, ? extends Aspect<? extends FormEditor>>) super.getAspectByName();
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<? extends Aspect<? extends FormEditor>> getAspects() {
    return (List<? extends Aspect<? extends FormEditor>>) super.getAspects();
  }

  @Override
  public String getFormAction() {
    return getContent().getString(FORM_ACTION);
  }


  @Override
  public Boolean isSpamProtectionEnabled() {
    return getContent().getBoolean(FORM_SPAM_PROTECTION);
  }
}
