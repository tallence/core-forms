package com.tallence.formeditor;

import com.tallence.formeditor.elements.FormElement;
import com.tallence.formeditor.parser.AbstractFormElementParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackages = "com.tallence.formeditor.parser.**")
public class FormEditorConfiguration {

  @Bean
  public FormElementFactory formElementFactory(List<AbstractFormElementParser<? extends FormElement<?>>> parsers) {
    return new FormElementFactory(parsers);
  }

}
