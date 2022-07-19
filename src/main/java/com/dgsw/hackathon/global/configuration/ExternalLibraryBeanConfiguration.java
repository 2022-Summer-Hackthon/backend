package com.dgsw.hackathon.global.configuration;

import com.dgsw.hackathon.global.properties.ScraperProperties;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@EnableConfigurationProperties
@Configuration
public class ExternalLibraryBeanConfiguration {
    @Bean
    public Komoran komoran() {
        return new Komoran(DEFAULT_MODEL.FULL);
    }

    @Bean
    public ChromeDriver chromeDriver() {
        ScraperProperties scraperProperties = scraperProperties();

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--incognito");

        if(scraperProperties.isUseHeadless())
            chromeOptions.addArguments("--headless");

        System.setProperty("webdriver.chrome.driver", scraperProperties.getChromeDriverPath());
        return new ChromeDriver(chromeOptions);
    }

    @Bean
    public ScraperProperties scraperProperties() {
        return new ScraperProperties();
    }

    @Bean
    public WebDriverWait webDriverWait() {
        return new WebDriverWait(chromeDriver(), Duration.ofSeconds(3));
    }
}
