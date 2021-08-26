package com.tallence.formeditor.studio.validator;

import com.coremedia.cap.common.CapConnection;
import com.coremedia.cap.multisite.SitesService;
import com.coremedia.cap.multisite.impl.MultiSiteConfiguration;
import com.tallence.formeditor.FormElementFactory;
import com.tallence.formeditor.contentbeans.FormEditor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Locale;
import java.util.function.Supplier;

@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackages = "com.tallence.formeditor.studio.validator.field")
@Import(MultiSiteConfiguration.class)
public class FormStudioValidatorConfiguration {

  @Bean
  public FormEditorValidator createFormEditorValidator(CapConnection connection, FormElementFactory formElementFactory,
                                                       ThreadLocal<Locale> localeThreadLocal, SitesService sitesService) {
    final var formEditorValidator = new FormEditorValidator(localeThreadLocal, formElementFactory, sitesService);

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
