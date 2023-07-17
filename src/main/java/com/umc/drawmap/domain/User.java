package com.umc.drawmap.domain;

import com.umc.drawmap.dto.UserResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "\"User\"")
@Getter
@AllArgsConstructor
@Setter
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "nick_name")
    private String nickName;
    @Column(name = "email")
    private String email;
    @Column(name = "profile_img")
    private String profileImg;
    @Column(name = "bike_category", length = 30)
    private String bike;
    @Column(name = "region", length = 10)
    private String region;
    @Column(name = "gender")
    private int gender;
    @Column(name="created_at")
    private LocalDateTime createdAt;

    // Token
    @Column(name = "refresh_token")
    private String refresh_token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_challenge_id")
    private UserChallenge userChallenge;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Scrap> scraps = new ArrayList<>();


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserCourse> userCourses = new ArrayList<>();

    public User() {};

    // Setter

}
