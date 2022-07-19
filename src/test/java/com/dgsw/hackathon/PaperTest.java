package com.dgsw.hackathon;

import com.dgsw.hackathon.domain.paper.presentation.dto.request.PaperDraftRequest;
import com.dgsw.hackathon.domain.paper.service.PaperServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class PaperTest {

    @Autowired
    private PaperServiceImpl paperService;

    @Test
    void testGithub() {
        paperService.requestScrapPaper(new PaperDraftRequest("https://github.com/iqpizza6349"));
    }
}
