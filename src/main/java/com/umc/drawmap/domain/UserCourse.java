package com.umc.drawmap.domain;

import com.umc.drawmap.dto.UserCourseReqDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCourse extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_course_id")
    private Long id;

    @Column(name = "user_course_title")
    private String userCourseTitle;

//    @Column(name = "user_course_area")
//    @Enumerated(EnumType.STRING)
//    private Region userCourseArea;

    @Column(name = "user_course_difficulty")
    private String userCourseDifficulty;

    @Column(name = "user_course_content")
    private String userCourseContent;

    @Column(name = "user_image")
    private String userImage;

    @Column(name = "files")
    private String files;

    @Column(columnDefinition = "INT DEFAULT 0")
    private int scrapCount;

    @Column(name = "sido")
    private String sido;

    @Column(name = "sgg")
    private String sgg;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private User user;

    public UserCourse update(String userCourseTitle, String sido, String sgg,
                             String userCourseDifficulty, String userCourseContent,
                             String files) {
        this.userCourseTitle = userCourseTitle;
        this.sido = sido;
        this.sgg = sgg;
        this.userCourseDifficulty = userCourseDifficulty;
        this.userCourseContent = userCourseContent;
        this.files = files;
        return this;
    }

    public void updateCount(int scrapCount) {
        this.scrapCount = scrapCount;
    }
}
