package com.tallence.formeditor.cae.config;

import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import com.coremedia.cache.Cache;
import com.coremedia.cache.CacheCapacityConfigurer;
import com.coremedia.springframework.xml.ResourceAwareXmlBeanDefinitionReader;
import com.tallence.formeditor.cae.handler.CaptchaService;
import com.tallence.formeditor.cae.handler.ReCaptchaServiceImpl;

import jakarta.servlet.annotation.MultipartConfig;


@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({
  FormEditorConfigurationProperties.class
})

@ComponentScan(basePackages = {
  "com.tallence.formeditor.cae"
})

@ImportResource(value = {
  "classpath:/com/tallence/formeditor/contentbeans/formeditor-contentbeans.xml",
  "classpath:/com/coremedia/blueprint/base/multisite/bpbase-multisite-cae-services.xml",
}, reader = ResourceAwareXmlBeanDefinitionReader.class)

@MultipartConfig(maxFileSize = 15000000)
public class FormEditorCaeConfig {

  private final FormEditorConfigurationProperties formEditorConfigurationProperties;


  public FormEditorCaeConfig(FormEditorConfigurationProperties formEditorConfigurationProperties) {
    this.formEditorConfigurationProperties = formEditorConfigurationProperties;
  }

  @Bean
  @Primary
  public StandardServletMultipartResolver multipartResolver() {
    return new StandardServletMultipartResolver();
  }


  @Bean
  public CacheCapacityConfigurer formEditorCacheCapacityConfigurer(Cache cache) {
    var ccc = new CacheCapacityConfigurer();
    ccc.setCache(cache);
    ccc.setCapacities(Map.of("com.tallence.formeditor.cae.serializer.FormConfigCacheKey", formEditorConfigurationProperties.getCacheCapacity()));
    return ccc;
  }


  @Bean
  @ConditionalOnMissingBean(CaptchaService.class)
  public CaptchaService captchaService() {
    ReCaptchaServiceImpl.ReCaptchaAuthentication authentication = new ReCaptchaServiceImpl.ReCaptchaAuthentication(
            formEditorConfigurationProperties.getWebsiteSecret(),
            formEditorConfigurationProperties.getServerSecret()
    );
    return new ReCaptchaServiceImpl(authentication);
  }
}
