package com.dgsw.hackathon.domain.paper.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public class DraftNumberResponse {
    @JsonProperty("draft_id")
    private final long draftId;
}
