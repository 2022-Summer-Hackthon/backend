package com.dgsw.hackathon.domain.paper.presentation;

import com.dgsw.hackathon.domain.paper.presentation.dto.request.PaperDraftRequest;
import com.dgsw.hackathon.domain.paper.presentation.dto.response.DraftNumberResponse;
import com.dgsw.hackathon.domain.paper.presentation.dto.response.PaperDraftResponse;
import com.dgsw.hackathon.domain.paper.presentation.dto.response.PaperFinishedResponse;
import com.dgsw.hackathon.domain.paper.service.PaperService;
import com.dgsw.hackathon.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Slf4j
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

    @PostMapping("/downloads")
    public PaperFinishedResponse uploadPaper(@RequestPart("front") MultipartFile front, @RequestPart("behind") MultipartFile behind) {
        return paperService.uploadPaper(front, behind);
    }

    @GetMapping("/downloads/{paper-id}/{type}")
    public ResponseEntity<Resource> getUploadedPaper(@PathVariable("paper-id") String id, @PathVariable("paper-side") String side) {
        log.info("request about {} side of {}", side, id);
        return paperService.getUploadedPaper(id, side);
    }
}
