package com.umc.drawmap.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Challenge extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_course_id")
    private Long id;

    @Column(name = "challenge_course_title")
    private String challengeCourseTitle;

    @Column(name = "challenge_course_area")
    private Region challengeCourseArea;

    @Column(name = "challenge_course_difficulty")
    private String challengeCourseDifficulty;

    @Column(name = "challenge_course_content")
    private String challengeCourseContent;
    @Column(name = "challenge_image")
    private String challengeImage;

    @Column(columnDefinition = "INT DEFAULT 0")
    private int scrapCount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_challenge_id")
    private UserChallenge userChallenge;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    private List<SpotImage> spotImages = new ArrayList<>();

    public Challenge update(String challengeCourseTitle, Region challengeCourseArea, String challengeCourseDifficulty, String challengeCourseContent, String challengeImage){
        this.challengeCourseTitle=challengeCourseTitle;
        this.challengeCourseArea=challengeCourseArea;
        this.challengeCourseDifficulty=challengeCourseDifficulty;
        this.challengeCourseContent=challengeCourseContent;
        this.challengeImage=challengeImage;
        return this;
    }

    public Challenge updateUserChallenge(UserChallenge userChallenge){
        this.userChallenge = userChallenge;
        return this;
    }
    public void updateCount(int scrapCount) {
        this.scrapCount = scrapCount;
    }
}
