package com.vqd.tme.na2a.config;

import com.vqd.tme.na2a.model.tme.TmeLoginCredentials;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix="tme.api")
public class TmeApiProperties {
    private String authUrl;
    private String authUser;
    private String authPass;
    private String productUrl;

    public TmeLoginCredentials getCredentials() {
        return new TmeLoginCredentials(authUser, authPass);
    }
}
