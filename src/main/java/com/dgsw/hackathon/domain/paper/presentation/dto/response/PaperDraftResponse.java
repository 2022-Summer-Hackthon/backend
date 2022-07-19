package com.dgsw.hackathon.domain.paper.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor @Getter
public class PaperDraftResponse {
    private final String name;

    @JsonProperty("job_type")
    private final String jobType;

    private final List<CarrierResponse> carriers;
}
