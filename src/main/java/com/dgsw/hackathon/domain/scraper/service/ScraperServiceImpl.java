package com.dgsw.hackathon.domain.scraper.service;

import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.Token;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ScraperServiceImpl implements ScraperService {

    private final ChromeDriver chromeDriver;
    private final WebDriverWait webDriverWait;
    private final Komoran komoran;

    private List<Token> joinNouns(List<Token> tokens) {
        List<Token> result = new ArrayList<>();

        StringBuilder appender = new StringBuilder();
        for(Token token: tokens) {
            if(token.getPos().startsWith("N")) {
                appender.append(token.getMorph()).append(" ");
            }else if(token.getPos().startsWith("XSN")) {
                appender.deleteCharAt(appender.toString().length() - 1).append(token.getMorph()).append(" ");
            }else {
                if(!appender.toString().equals("")) {
                    result.add(new Token(appender.deleteCharAt(appender.toString().length() - 1).toString(), "NNP", 0, 0));
                    appender = new StringBuilder();
                }
                result.add(token);
            }
        }

        return result;
    }

    private List<Token> filterTokens(List<Token> tokens) {
        return tokens.stream()
                .filter(it -> it.getMorph().length() >= 3)
                .filter(it -> !it.getPos().equals("SN"))
                .filter(it -> !it.getPos().startsWith("S") || it.getPos().equals("SL"))
                .collect(Collectors.toList());
    }

    @Override
    public List<Token> scrapGithub(String name) {
        chromeDriver.get("https://github.com/" + name);

        webDriverWait.until(driver -> driver.findElements(By.cssSelector(".Layout.Layout--flowRow-until-md.Layout--sidebarPosition-start.Layout--sidebarPosition-flowRow-start")).size() != 0);

        String data = Arrays.stream(chromeDriver
                .findElements(By.cssSelector(".Layout.Layout--flowRow-until-md.Layout--sidebarPosition-start.Layout--sidebarPosition-flowRow-start"))
                .get(1)
                .getText().split("\n"))
                .collect(Collectors.joining(" "));

        System.out.println(data);

        List<Token> result = komoran.analyze(data).getTokenList();
        result = joinNouns(result);
        result = filterTokens(result);

        result.forEach(token -> {
            System.out.printf("%s / %s\n", token.getMorph(), token.getPos());
        });

        return result;
    }

}
