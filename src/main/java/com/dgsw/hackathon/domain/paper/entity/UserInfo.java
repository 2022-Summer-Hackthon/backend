package com.dgsw.hackathon.domain.paper.entity;

import com.dgsw.hackathon.domain.paper.type.InfoType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@AllArgsConstructor @NoArgsConstructor @Getter
@Entity
public class UserInfo {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn
    private Paper paper;
    public void setPaper(Paper paper) {
        this.paper = paper;
    }

    @Enumerated(EnumType.STRING)
    private InfoType infoType;

    private String data;

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof UserInfo)) return false;

        return this.data.equals(((UserInfo) obj).data) && this.infoType.equals(((UserInfo) obj).infoType);
    }

}
