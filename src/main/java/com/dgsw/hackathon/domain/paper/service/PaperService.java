package com.dgsw.hackathon.domain.paper.service;

import com.dgsw.hackathon.domain.paper.presentation.dto.request.PaperDraftRequest;
import com.dgsw.hackathon.domain.paper.presentation.dto.response.DraftNumberResponse;
import com.dgsw.hackathon.domain.paper.presentation.dto.response.PaperDraftResponse;
import com.dgsw.hackathon.domain.paper.presentation.dto.response.PaperFinishedResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PaperService {
    DraftNumberResponse requestScrapPaper(PaperDraftRequest paperDraftRequest);

    PaperDraftResponse requestFinishedPaper(long draftNumber);

    PaperFinishedResponse uploadPaper(List<MultipartFile> files);

    ResponseEntity<Resource> getUploadedPaper(String id, String side);

}
