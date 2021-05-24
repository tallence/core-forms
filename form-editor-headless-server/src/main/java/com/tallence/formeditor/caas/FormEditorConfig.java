package com.tallence.formeditor.caas;

import com.coremedia.caas.wiring.ProvidesTypeNameResolver;
import com.coremedia.caas.wiring.TypeNameResolver;
import com.coremedia.cap.content.ContentRepository;
import com.coremedia.springframework.xml.ResourceAwareXmlBeanDefinitionReader;
import com.tallence.formeditor.caas.adapter.FormEditorAdapterFactory;
import com.tallence.formeditor.cae.FormElementFactory;
import com.tallence.formeditor.cae.elements.CheckBoxesGroup;
import com.tallence.formeditor.cae.elements.ComplexValue;
import com.tallence.formeditor.cae.elements.ConsentFormCheckBox;
import com.tallence.formeditor.cae.elements.DateField;
import com.tallence.formeditor.cae.elements.FaxField;
import com.tallence.formeditor.cae.elements.FileUpload;
import com.tallence.formeditor.cae.elements.FormElement;
import com.tallence.formeditor.cae.elements.NumberField;
import com.tallence.formeditor.cae.elements.PhoneField;
import com.tallence.formeditor.cae.elements.RadioButtonGroup;
import com.tallence.formeditor.cae.elements.SelectBox;
import com.tallence.formeditor.cae.elements.StreetNumberField;
import com.tallence.formeditor.cae.elements.TextArea;
import com.tallence.formeditor.cae.elements.TextField;
import com.tallence.formeditor.cae.elements.TextOnly;
import com.tallence.formeditor.cae.elements.UsersMail;
import com.tallence.formeditor.cae.elements.ZipField;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration(proxyBeanMethods = false)
public class FormEditorConfig {

  @Bean
  public FormEditorAdapterFactory formEditorAdapter(FormElementFactory formElementFactory) {
    return new FormEditorAdapterFactory(formElementFactory);
  }

  @Bean
  public ProvidesTypeNameResolver providesFormBeanTypeNameResolver(FormElementFactory formElementFactory) {
    return typeName ->
            formElementFactory.getTypes().contains(typeName)
                    || "FormElement".equals(typeName)
                    || "RadioButtonGroup".equals(typeName) //The parsers key is different to the forms name
                    || "CheckBoxesGroup".equals(typeName) //The parsers key is different to the forms name
                    ? Optional.of(true)
                    : Optional.empty();
  }

  @Bean
  public TypeNameResolver<FormElement> formElementTypeNameResolver() {
    return element -> {
      String simpleClassName = element.getClass().getSimpleName();
      return Optional.of(simpleClassName+ "Impl");
    };
  }
}
