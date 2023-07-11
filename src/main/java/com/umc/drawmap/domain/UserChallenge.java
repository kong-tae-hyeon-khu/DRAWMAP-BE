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

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "challenge_course_id")
    private Long challengeCourseId;

    @Column(name = "challenge_comment")
    private String challengeComment;

    @Column(name = "challenge_star")
    private int challengeStar;

    @Column(name = "challenge_image")
    private String challengeImage;

}
