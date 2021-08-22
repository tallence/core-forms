package com.tallence.formeditor.caas;

import com.coremedia.caas.wiring.ProvidesTypeNameResolver;
import com.coremedia.caas.wiring.TypeNameResolver;
import com.coremedia.cap.multisite.SitesService;
import com.coremedia.cap.multisite.impl.MultiSiteConfiguration;
import com.tallence.formeditor.FormElementFactory;
import com.tallence.formeditor.caas.adapter.FormEditorAdapterFactory;
import com.tallence.formeditor.elements.FormElement;
import com.tallence.formeditor.validator.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * TODO this is still WIP! Do not use yet
 */
@Configuration(proxyBeanMethods = false)
@Import(value = {MultiSiteConfiguration.class,})
public class FormEditorConfig {

  private static final Set<String> VALIDATORS = Stream.of(
          "Validator",
          "SizeValidator",
          "NumberValidator",
          "UsersMailValidator",
          "TextValidator",
          "TextOnlyValidator",
          "SelectBoxValidator",
          "RadioButtonGroupValidator",
          "FileUploadValidator",
          "DateFieldValidator",
          "ConsentFormCheckboxValidator",
          "CheckBoxesGroupValidator"

  ).collect(Collectors.toSet());

  @Bean
  public FormEditorAdapterFactory formEditorAdapter(FormElementFactory formElementFactory, SitesService sitesService) {
    return new FormEditorAdapterFactory(formElementFactory, sitesService);
  }

  @Bean
  public ProvidesTypeNameResolver providesFormBeanTypeNameResolver(FormElementFactory formElementFactory) {
    return name -> Optional.of(true);
    /*
    TODO return a proper ProvidesTypeNameResolver
    return Name ->
            formElementFactory.getTypes().contains(Name)
                    || "FormElement".equals(Name)
                    || "RadioButtonGroup".equals(Name) //The parsers key is different to the forms name
                    || "CheckBoxesGroup".equals(Name) //The parsers key is different to the forms name
                    ? Optional.of(true)
                    : Optional.empty();
    */
  }

  @Bean
  public ProvidesTypeNameResolver providesFormValidatorTypeNameResolver(FormElementFactory formElementFactory) {
    return Name ->
            VALIDATORS.contains(Name)
                    ? Optional.of(true)
                    : Optional.empty();
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
