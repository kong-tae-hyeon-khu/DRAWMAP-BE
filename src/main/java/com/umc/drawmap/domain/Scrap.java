package com.umc.drawmap.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Scrap {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="scrap_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_course_id")
    private UserCourse userCourse;
}
