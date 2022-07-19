package com.dgsw.hackathon.global.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@ConfigurationProperties(prefix = "scraper")
public class ScraperProperties {
    private String chromeDriverPath;
    private boolean useHeadless;
}
