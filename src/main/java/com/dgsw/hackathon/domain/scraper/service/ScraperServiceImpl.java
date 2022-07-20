package com.dgsw.hackathon.domain.scraper.service;

import com.dgsw.hackathon.domain.paper.entity.UserInfo;
import com.dgsw.hackathon.domain.paper.type.InfoType;
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
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
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

    @Override
    public List<UserInfo> scrapTelNumbersFromCustomSite(String url) {
        chromeDriver.get(url);

        webDriverWait.until(driver -> driver.findElement(By.tagName("body")).getText().length() >= 20);

        String text = chromeDriver.findElement(By.tagName("body")).getText();

        List<UserInfo> result = new ArrayList<>();

        // telephones
        Pattern telephoneFinderPattern = Pattern.compile("\\d+-\\d+-\\d+");
        Pattern telephoneExactlyPattern = Pattern.compile("\\d{2,3}-\\d{3,4}-\\d{3,4}");
        List<UserInfo> telephones = telephoneFinderPattern.matcher(text).results().map(MatchResult::group)
                .filter(it -> telephoneExactlyPattern.matcher(it).matches())
                .map(it -> UserInfo.builder()
                                .infoType(InfoType.TEL)
                                .data(it)
                                .build()
                        )
                .collect(Collectors.toList());
        result.addAll(telephones);

        // e-mail
        Pattern emailPattern = Pattern.compile("([a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z0-9_-]+)");
        List<UserInfo> emails = emailPattern.matcher(text).results().map(MatchResult::group)
                .map(it -> UserInfo.builder()
                        .infoType(InfoType.EMAIL)
                        .data(it)
                        .build()
                )
                .collect(Collectors.toList());
        result.addAll(emails);

        Pattern githubPattern = Pattern.compile("https:\\/\\/github\\.com\\/.+");
        List<UserInfo> githubUrls = githubPattern.matcher(text).results().map(MatchResult::group)
                .map(it -> UserInfo.builder()
                        .infoType(InfoType.GITHUB)
                        .data(it)
                        .build()
                )
                .collect(Collectors.toList());
        result.addAll(githubUrls);

        return result;
    }

}
