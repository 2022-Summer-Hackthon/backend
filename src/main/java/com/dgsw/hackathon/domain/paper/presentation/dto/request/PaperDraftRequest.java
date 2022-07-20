package com.dgsw.hackathon.domain.paper.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
@Getter
public class PaperDraftRequest {
    @JsonProperty("github_url")
    private String githubUrl;

    @JsonProperty("custom_url")
    private String customUrl;
}
