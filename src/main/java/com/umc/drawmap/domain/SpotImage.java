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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "spot_image_id")
    private Long id;

    @Column(name = "spot_title")
    private String spotTitle;

    @Column(name = "spot_area")
    private String spotArea;
    @Column(name = "spot_content")
    private String spotContent;
    @Column(name = "spot_image")
    private String spotImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_course_id")
    private Challenge challenge;



}
