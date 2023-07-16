package com.umc.drawmap.domain;

import lombok.*;

import javax.persistence.*;


@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class UserChallenge extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_challenge_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "challenge_course_id")
    private Challenge challenge;

    @Column(name = "challenge_comment")
    private String challengeComment;

    @Column(name = "challenge_star")
    private int challengeStar;

    @Column(name = "challenge_image")
    private String challengeImage;


    public void update(String challengeComment, int challengeStar, String challengeImage) {
        this.challengeComment = challengeComment;
        this.challengeStar = challengeStar;
        this.challengeImage = challengeImage;
    }


}
