package com.umc.drawmap.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpotImage extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spot_image_id")
    private Long id;

    @Column(name = "spot_title")
    private String spotTitle;

    @Column(name = "spot_content")
    private String spotContent;
    @Column(name = "spot_image")
    private String spotImage;

    @Column(name = "sido")
    private String sido;

    @Column(name = "sgg")
    private String sgg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_course_id")
    private Challenge challenge;

    public SpotImage update(String spotTitle, String sido, String sgg, String spotContent, String spotImage){
        this.spotTitle=spotTitle;
        this.sido = sido;
        this.sgg = sgg;
        this.spotContent=spotContent;
        this.spotImage=spotImage;
        return this;
    }



}
