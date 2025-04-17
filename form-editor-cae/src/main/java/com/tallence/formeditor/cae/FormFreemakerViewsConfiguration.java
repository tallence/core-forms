package com.tallence.formeditor.cae;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.coremedia.blueprint.common.services.context.CurrentContextService;
import com.coremedia.springframework.customizer.Customize;
import com.coremedia.springframework.xml.ResourceAwareXmlBeanDefinitionReader;
import com.tallence.formeditor.FormElementFactory;
import com.tallence.formeditor.cae.handler.ReCaptchaService;

@Configuration(proxyBeanMethods = false)

@ImportResource(value = {
        "classpath:/com/coremedia/cae/view-freemarker-services.xml"
}, reader = ResourceAwareXmlBeanDefinitionReader.class)

public class FormFreemakerViewsConfiguration {

    @Bean
    public FormFreemarkerFacade formFreemarkerFacade(FormElementFactory formElementFactory,
                                                     CurrentContextService currentContextService,
                                                     ReCaptchaService reCaptchaService) {
          return new FormFreemarkerFacade(formElementFactory,reCaptchaService,currentContextService);
    }


    @Bean(autowireCandidate = false)
    @Customize("freemarkerSharedVariables")
    public Map<String, FormFreemarkerFacade> formFreemarkerSharedVariablesCustomizer(FormFreemarkerFacade formFreemarkerFacade) {
        return Map.of("formFreemarkerFacade", formFreemarkerFacade);
    }


    @Customize("freemarkerConfigurer.autoImports")
    @Bean(autowireCandidate = false)
    public Map<String, String> amFreemarkerConfigurerAutoImportsCustomizer() {
        return Map.of("form", "/lib/form/form.ftl");
    }
}
