package com.tallence.formeditor.cae.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "tallence.formeditor"
)
public class FormEditorConfigurationProperties {
    private long cacheCapacity;
    private String websiteSecret = "CHANGE_ME";
    private String serverSecret = "CHANGE_ME";

    public FormEditorConfigurationProperties() {
        this.cacheCapacity = 100L;
    }

    public String getWebsiteSecret() {
        return websiteSecret;
    }

    public void setWebsiteSecret(String websiteSecret) {
        this.websiteSecret = websiteSecret;
    }

    public String getServerSecret() {
        return serverSecret;
    }

    public void setServerSecret(String serverSecret) {
        this.serverSecret = serverSecret;
    }

    public long getCacheCapacity() {
        return cacheCapacity;
    }

    public void setCacheCapacity(long cacheCapacity) {
        this.cacheCapacity = cacheCapacity;
    }
}
