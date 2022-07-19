package com.dgsw.hackathon.domain.paper.entity;

import com.dgsw.hackathon.domain.paper.type.CarrierCategory;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor @NoArgsConstructor @Getter
@Builder
@ToString
@Entity
public class Carrier {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn
    private Paper paper;
    public void setPaper(Paper paper) {
        this.paper = paper;
    }

    private Boolean isProgress;

    private String carrierName;

    @Enumerated(EnumType.STRING)
    private CarrierCategory category;

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Carrier)) return false;

        return this.carrierName.equals(((Carrier) obj).carrierName);
    }

    @Override
    public int hashCode() {
        return this.carrierName.hashCode();
    }
}
