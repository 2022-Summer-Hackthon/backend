package com.dgsw.hackathon.domain.paper.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public enum JobCategory {
    BACKEND("백엔드", new String[]{ "Spring", "Node", "REST", "API", "Server" }),
    FRONTEND("프론트엔드", new String[]{ "html", "redux", "react", "vue", "javascript", "typescript", "knex" }),
    ANDROID("안드로이드", new String[]{ "Android", "Android Studio", "google" }),
    IOS("iOS", new String[]{ "iOS", "Swift", "objective-c", "apple" }),
    AI("AI", new String[]{ "python", "ai", "numpy", "ml", "machine", "learning" });

    private String name;
    private String[] keywords;
}
