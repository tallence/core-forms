package com.tallence.formeditor.caas;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import(FormEditorConfig.class)
public class FormEditorAutoConfiguration {
}
