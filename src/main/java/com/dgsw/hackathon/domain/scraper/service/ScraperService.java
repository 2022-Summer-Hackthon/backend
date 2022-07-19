package com.dgsw.hackathon.domain.scraper.service;

import kr.co.shineware.nlp.komoran.model.Token;

import java.util.List;

public interface ScraperService {
    public List<Token> scrapGithub(String name);
}
