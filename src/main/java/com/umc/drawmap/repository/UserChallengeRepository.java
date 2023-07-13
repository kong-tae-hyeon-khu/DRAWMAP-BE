package com.umc.drawmap.repository;


import com.umc.drawmap.domain.Challenge;
import com.umc.drawmap.domain.User;
import com.umc.drawmap.domain.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserChallengeRepository extends JpaRepository<UserChallenge,Long>{
    Optional<UserChallenge> findUserChallengeByUserAndChallenge(User user, Challenge challenge);
    List<UserChallenge> findAll();
}

