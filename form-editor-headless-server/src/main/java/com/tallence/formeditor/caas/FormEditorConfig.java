package com.tallence.formeditor.caas;

import com.coremedia.caas.wiring.ProvidesTypeNameResolver;
import com.coremedia.caas.wiring.TypeNameResolver;
import com.tallence.formeditor.caas.adapter.FormEditorAdapterFactory;
import com.tallence.formeditor.cae.FormElementFactory;
import com.tallence.formeditor.cae.elements.FormElement;
import com.tallence.formeditor.cae.validator.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration(proxyBeanMethods = false)
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
  public FormEditorAdapterFactory formEditorAdapter(FormElementFactory formElementFactory) {
    return new FormEditorAdapterFactory(formElementFactory);
  }

  @Bean
  public ProvidesTypeNameResolver providesFormBeanTypeNameResolver(FormElementFactory formElementFactory) {
    return Name ->
            formElementFactory.getTypes().contains(Name)
                    || "FormElement".equals(Name)
                    || "RadioButtonGroup".equals(Name) //The parsers key is different to the forms name
                    || "CheckBoxesGroup".equals(Name) //The parsers key is different to the forms name
                    ? Optional.of(true)
                    : Optional.empty();
  }

  @Bean
  public ProvidesTypeNameResolver providesFormValidatorTypeNameResolver(FormElementFactory formElementFactory) {
    return Name ->
            VALIDATORS.contains(Name)
                    ? Optional.of(true)
                    : Optional.empty();
  }

  @Bean
  public TypeNameResolver<FormElement> formElementTypeNameResolver() {
    return element -> {
      String simpleClassName = element.getClass().getSimpleName();
      return Optional.of(simpleClassName + "Impl");
    };
  }

  @Bean
  public TypeNameResolver<Validator> validatorTypeNameResolver() {
    return element -> {
      String simpleClassName = element.getClass().getSimpleName();
      return Optional.of(simpleClassName);
    };
  }
}
