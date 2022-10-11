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
package com.tallence.formeditor.studio.validator;

import com.coremedia.cap.common.CapConnection;
import com.coremedia.cap.multisite.SitesService;
import com.coremedia.cap.multisite.impl.MultiSiteConfiguration;
import com.tallence.formeditor.FormElementFactory;
import com.tallence.formeditor.studio.validator.field.ComplexValidator;
import com.tallence.formeditor.studio.validator.field.FieldValidator;
import com.tallence.formeditor.contentbeans.FormEditor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackages = "com.tallence.formeditor.studio.validator.field")
@Import(MultiSiteConfiguration.class)
public class FormStudioValidatorConfiguration {

  @Bean
  public FormEditorValidator createFormEditorValidator(CapConnection connection, FormElementFactory formElementFactory,
                                                       ThreadLocal<Locale> localeThreadLocal, SitesService sitesService,
                                                       List<FieldValidator> fieldValidators,
                                                       List<ComplexValidator> complexValidators) {
    final var formEditorValidator = new FormEditorValidator(localeThreadLocal, formElementFactory, sitesService,
            fieldValidators, complexValidators);

    formEditorValidator.setContentType(FormEditor.NAME);
    formEditorValidator.setConnection(connection);
    formEditorValidator.setValidatingSubtypes(true);
    return formEditorValidator;
  }

  @Bean
  public ThreadLocal<Locale> localeThreadLocal() {
    return ThreadLocal.withInitial(() -> Locale.US);
  }

  /**
   * Used as a Wrapper around the CoreMedia component dependent locale resolver: The CurrentContextService in the CAE,
   * this threadLocal based construct in the Studio-Lib.
   */
  @Bean
  public Supplier<Locale> localeSupplier(ThreadLocal<Locale> localeThreadLocal) {
    return localeThreadLocal::get;
  }

}
