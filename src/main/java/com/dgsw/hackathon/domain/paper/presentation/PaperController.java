package com.dgsw.hackathon.domain.paper.presentation;

import com.dgsw.hackathon.domain.paper.presentation.dto.request.PaperDraftRequest;
import com.dgsw.hackathon.domain.paper.presentation.dto.response.DraftNumberResponse;
import com.dgsw.hackathon.domain.paper.presentation.dto.response.PaperDraftResponse;
import com.dgsw.hackathon.domain.paper.service.PaperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/draft")
@RestController
public class PaperController {
    private final PaperService paperService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    public DraftNumberResponse requestDraft(@RequestBody PaperDraftRequest request) {
        return paperService.requestScrapPaper(request);
    }

    @GetMapping("/{draft-id}")
    public PaperDraftResponse getDraft(@PathVariable("draft-id") long draftId) {
        return paperService.requestFinishedPaper(draftId);
    }
}
