package com.umc.drawmap.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "\"User\"")
@Getter
@AllArgsConstructor
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private int id;

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

    public User() {};

    // Setter

}
