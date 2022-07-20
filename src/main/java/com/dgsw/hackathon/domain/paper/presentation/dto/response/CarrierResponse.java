package com.dgsw.hackathon.domain.paper.presentation.dto.response;

import com.dgsw.hackathon.domain.paper.type.CarrierCategory;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public class CarrierResponse {
    private CarrierCategory category;

    @JsonProperty("carrier_name")
    private String carrierName;

    @JsonProperty("is_progress")
    private boolean isProgress;
}
