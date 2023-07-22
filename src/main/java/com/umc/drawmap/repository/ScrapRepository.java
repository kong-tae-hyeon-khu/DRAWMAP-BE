package com.umc.drawmap.repository;

import com.umc.drawmap.domain.Challenge;
import com.umc.drawmap.domain.Scrap;
import com.umc.drawmap.domain.User;
import com.umc.drawmap.domain.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long>{

    Boolean existsScrapByUserAndChallenge(User user, Challenge challenge);
    Optional<Scrap> findByUserAndChallenge(User user, Challenge challenge);
    Optional<Scrap> findByUserAndUserCourse(User user, UserCourse userCourse);

    List<Scrap> findAllByUser(User user);

    Boolean existsScrapByUserAndUserCourse(User user, UserCourse userCourse);
}