package com.dgsw.hackathon.domain.paper.service;

import com.dgsw.hackathon.domain.paper.presentation.dto.request.PaperDraftRequest;
import com.dgsw.hackathon.domain.paper.presentation.dto.response.DraftNumberResponse;
import com.dgsw.hackathon.domain.paper.presentation.dto.response.PaperDraftResponse;

public interface PaperService {
    DraftNumberResponse requestScrapPaper(PaperDraftRequest paperDraftRequest);

    PaperDraftResponse requestFinishedPaper(long draftNumber);
}
