package com.umc.drawmap.domain;

import lombok.*;

import javax.persistence.*;


@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Challenge extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "challenge_course_id")
    private Long id;

    @Column(name = "challenge_course_title")
    private String challengeCourseTitle;

    @Column(name = "challenge_course_area")
    private String challengeCourseArea;

    @Column(name = "challenge_course_difficulty")
    private String challengeCourseDifficulty;

    @Column(name = "challenge_course_content")
    private String challengeCourseContent;
    @Column(name = "challenge_image")
    private String challengeImage;

    @Column(columnDefinition = "INT DEFAULT 0")
    private int scrapCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private User user;

}
