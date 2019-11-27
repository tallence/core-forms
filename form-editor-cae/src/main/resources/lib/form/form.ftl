<#ftl strip_whitespace=true>
<#-- @ftlvariable name="formFreemarkerFacade" type="com.tallence.formeditor.cae.FormFreemarkerFacade" -->

<#function parseFormElements formEditor>
  <#return formFreemarkerFacade.parseFormElements(formEditor)>
</#function>

<#function reCaptchaWebsiteSecret>
  <#return formFreemarkerFacade.getReCaptchaWebsiteSecretForSite()>
</#function>
