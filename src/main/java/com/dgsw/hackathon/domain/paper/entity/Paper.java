package com.dgsw.hackathon.domain.paper.entity;

import com.dgsw.hackathon.domain.paper.type.JobCategory;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Builder
@ToString
@Entity
public class Paper {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private JobCategory jobCategory;

    @OneToMany(mappedBy = "paper")
    private List<Carrier> carriers;
    public void addCarrier(Carrier carrier) {
        carrier.setPaper(this);
        this.carriers.add(carrier);
    }

    @OneToMany(mappedBy = "paper")
    private List<UserInfo> userInfoList;
    public void addUserInfo(UserInfo userInfo) {
        userInfo.setPaper(this);
        this.userInfoList.add(userInfo);
    }
}
