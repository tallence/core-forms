package com.tallence.formeditor.caas;

import com.coremedia.caas.wiring.ProvidesTypeNameResolver;
import com.coremedia.caas.wiring.TypeNameResolver;
import com.coremedia.cap.multisite.SitesService;
import com.coremedia.cap.multisite.impl.MultiSiteConfiguration;
import com.tallence.formeditor.FormElementFactory;
import com.tallence.formeditor.caas.adapter.FormEditorAdapterFactory;
import com.tallence.formeditor.elements.FormElement;
import com.tallence.formeditor.validator.Validator;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * TODO this is still WIP! Do not use yet
 */
@Configuration(proxyBeanMethods = false)
@Import(value = {MultiSiteConfiguration.class,})
public class FormEditorConfig {

  @Bean
  public ThreadLocal<Locale> localeThreadLocal() {
    return ThreadLocal.withInitial(() -> Locale.US);
  }

  /**
   * Used as a Wrapper around the CoreMedia component dependent locale resolver: The CurrentContextService in the CAE,
   * this threadLocal based construct in the HeadlessServer.
   */
  @Bean
  public Supplier<Locale> localeSupplier(ThreadLocal<Locale> localeThreadLocal) {
    return localeThreadLocal::get;
  }

  @Bean
  public FormEditorAdapterFactory formEditorAdapter(FormElementFactory formElementFactory, SitesService sitesService, ThreadLocal<Locale> localeThreadLocal) {
    return new FormEditorAdapterFactory(formElementFactory, sitesService, localeThreadLocal);
  }

  @Bean
  public ProvidesTypeNameResolver providesFormBeanTypeNameResolver() {

    var reflections = new Reflections(new ConfigurationBuilder().forPackages(FormElement.class.getPackageName()));
    final var formElementTypes = reflections.getSubTypesOf(FormElement.class).stream()
            .map(Class::getSimpleName)
            .collect(Collectors.toList());
    formElementTypes.add(FormElement.class.getSimpleName());

    return name -> formElementTypes.contains(name) ? Optional.of(true): Optional.empty();
  }

  @Bean
  public ProvidesTypeNameResolver providesFormValidatorTypeNameResolver() {

    var reflections = new Reflections(new ConfigurationBuilder().forPackages(Validator.class.getPackageName()));
    final var validatorTypes = reflections.getSubTypesOf(Validator.class).stream()
            .map(Class::getSimpleName)
            .collect(Collectors.toList());
    validatorTypes.add(Validator.class.getSimpleName());
    validatorTypes.add("SizeValidator");

    return name -> validatorTypes.contains(name) ? Optional.of(true): Optional.empty();
  }

  @Bean
  public TypeNameResolver<FormElement<?>> formElementTypeNameResolver() {
    return element -> {
      String simpleClassName = element.getClass().getSimpleName();
      return Optional.of(simpleClassName + "Impl");
    };
  }

  @Bean
  public TypeNameResolver<Validator<?>> validatorTypeNameResolver() {
    return element -> {
      String simpleClassName = element.getClass().getSimpleName();
      return Optional.of(simpleClassName);
    };
  }
}
