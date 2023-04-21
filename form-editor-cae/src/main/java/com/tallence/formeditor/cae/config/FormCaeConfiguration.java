/*
 * Copyright 2023 Tallence AG
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
package com.tallence.formeditor.cae.config;

import com.tallence.formeditor.cae.handler.CaptchaService;
import com.tallence.formeditor.cae.handler.ReCaptchaServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FormCaeConfiguration {

  @Bean
  @ConditionalOnMissingBean(CaptchaService.class)
  public ReCaptchaServiceImpl captchaService(@Value("${google.reCaptcha.website-secret}") String websiteSecret,
                                               @Value("${google.reCaptcha.server-secret}") String serverSecret) {

    var auth = new ReCaptchaServiceImpl.ReCaptchaAuthentication(websiteSecret, serverSecret);

    return new ReCaptchaServiceImpl(auth);
  }
}
