package com.dgsw.hackathon.domain.paper.service;

import com.dgsw.hackathon.domain.paper.entity.Carrier;
import com.dgsw.hackathon.domain.paper.entity.Paper;
import com.dgsw.hackathon.domain.paper.exception.PaperNotFoundException;
import com.dgsw.hackathon.domain.paper.presentation.dto.request.PaperDraftRequest;
import com.dgsw.hackathon.domain.paper.presentation.dto.response.*;
import com.dgsw.hackathon.domain.paper.repository.CarrierRepository;
import com.dgsw.hackathon.domain.paper.repository.PaperRepository;
import com.dgsw.hackathon.domain.paper.repository.UserInfoRepository;
import com.dgsw.hackathon.domain.paper.type.CarrierCategory;
import com.dgsw.hackathon.domain.paper.type.JobCategory;
import com.dgsw.hackathon.domain.scraper.service.ScraperService;
import com.dgsw.hackathon.global.exception.BusinessException;
import kr.co.shineware.nlp.komoran.model.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PaperServiceImpl implements PaperService {
    private final ScraperService scraperService;
    private final PaperRepository paperRepository;
    private final CarrierRepository carrierRepository;
    private final UserInfoRepository userInfoRepository;

    private String getNameFromGithubTokens(List<Token> githubTokens) {
        return githubTokens.get(0).getMorph();
    }

    private JobCategory getJobCategoryFromTokens(List<Token> tokens) {
        int[] frequencies = new int[JobCategory.values().length];

        AtomicInteger categoryIndex = new AtomicInteger();
        Arrays.stream(JobCategory.values()).forEach(category -> {
            Arrays.stream(category.getKeywords()).forEach(keyword -> {
                frequencies[categoryIndex.get()] += tokens.stream().filter(it -> it.getMorph().toLowerCase().equals(keyword.toLowerCase())).count();
            });

            categoryIndex.incrementAndGet();
        });

        int maxIndex = 0;
        for(int i = 0; i < frequencies.length; i++) {
            if(frequencies[maxIndex] < frequencies[i])
                maxIndex = i;
        }

        return JobCategory.values()[maxIndex];
    }

    private List<Carrier> getCarriersFromTokens(List<Token> tokens) {
        List<Carrier> result = new ArrayList<>();

        for(int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if(token.getMorph().contains("팀") ||
                    (token.getMorph().toLowerCase().contains("team") && !token.getMorph().toLowerCase().equals("team"))
            ) {
                result.add(Carrier.builder()
                        .carrierName(token.getMorph())
                        .category(CarrierCategory.COMPANY)
                        .isProgress(true)
                        .build());
            }else if(token.getMorph().contains("학교") ||
                    (token.getMorph().toLowerCase().contains("school") && !token.getMorph().toLowerCase().equals("school"))
            ) {
                result.add(Carrier.builder()
                        .carrierName(token.getMorph())
                        .category(CarrierCategory.GRADUATE)
                        .isProgress(true)
                        .build());
            }

        }

        return result.stream().distinct().collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DraftNumberResponse requestScrapPaper(PaperDraftRequest paperDraftRequest) {
        String[] githubUrlTokens = paperDraftRequest.getGithubUrl().split("/");
        Paper paper = Paper.builder().build();

        List<Token> tokens = scraperService.scrapGithub(githubUrlTokens[githubUrlTokens.length - 1]);

        JobCategory jobCategory = getJobCategoryFromTokens(tokens);

        paper.setName(getNameFromGithubTokens(tokens));
        paper.setJobCategory(jobCategory);
        paper.setCarriers(new ArrayList<>());
        paper.setUserInfoList(new ArrayList<>());

        Paper savedPaper = paperRepository.save(paper);
        getCarriersFromTokens(tokens).forEach(it ->
                savedPaper.addCarrier(carrierRepository.save(it))
        );

        scraperService.scrapTelNumbersFromCustomSite(paperDraftRequest.getCustomUrl()).forEach(it -> {
            savedPaper.addUserInfo(userInfoRepository.save(it));
        });

        return new DraftNumberResponse(savedPaper.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public PaperDraftResponse requestFinishedPaper(long draftNumber) {
        Paper paper = paperRepository.findById(draftNumber)
                .orElseThrow(PaperNotFoundException::new);

        return new PaperDraftResponse(paper.getName(), paper.getJobCategory().getName(),
                paper.getCarriers().stream()
                        .map(it -> new CarrierResponse(
                            it.getCategory(),
                                it.getCarrierName(),
                                it.getIsProgress()
                        ))
                        .collect(Collectors.toList()),
                paper.getUserInfoList().stream()
                        .map(it -> new UserInfoResponse(
                                it.getInfoType(), it.getData())
                        )
                        .collect(Collectors.toList())
        );

    }

    @Override
    public PaperFinishedResponse uploadPaper(List<MultipartFile> files) {
        try {
            File baseFolder = new File("papers");
            if(!baseFolder.exists()) baseFolder.mkdirs();

            String uid = UUID.randomUUID().toString();
            MultipartFile front = files.get(0);
            front.transferTo(new File(baseFolder.getPath() + "/" + uid + "_front"));

            MultipartFile behind = files.get(1);
            behind.transferTo(new File(baseFolder.getPath() + "/" + uid + "_behind"));

            return new PaperFinishedResponse(uid);
        }catch (Exception ex) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "something went wrong");
        }
    }

    @Override
    public ResponseEntity<Resource> getUploadedPaper(String id, String side) {
        try {
            String fileName = String.format("%s_%s.png", id, side);
            Path path = Paths.get("papers/" + fileName);
            String contentType = Files.probeContentType(path);
            // header를 통해서 다운로드 되는 파일의 정보를 설정한다.
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename(fileName, StandardCharsets.UTF_8)
                    .build());
            headers.add(HttpHeaders.CONTENT_TYPE, contentType);

            Resource resource = new InputStreamResource(Files.newInputStream(path));
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        }catch (Exception ex) {
            throw new BusinessException(HttpStatus.NOT_FOUND, "resource not found");
        }
    }
}
