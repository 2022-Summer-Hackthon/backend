package com.dgsw.hackathon.domain.paper.presentation.dto.response;

import com.dgsw.hackathon.domain.paper.type.InfoType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor @NoArgsConstructor @Getter
public class UserInfoResponse {
    @JsonProperty("type")
    private InfoType infoType;

    private String value;
}
