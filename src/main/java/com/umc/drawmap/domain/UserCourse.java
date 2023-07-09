package com.umc.drawmap.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCourse extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_course_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_course_title")
    private String userCourseTitle;

    @Column(name = "user_course_area")
    private String userCourseArea;

    @Column(name = "user_course_difficulty")
    private String userCourseDifficulty;

    @Column(name = "user_course_ontent")
    private String userCourseContent;
    @Column(name = "user_image")
    private String userImage;

    @Column(name = "files")
    private String files;

    @Column(columnDefinition = "INT DEFAULT 0")
    private int scrapCount;


}
