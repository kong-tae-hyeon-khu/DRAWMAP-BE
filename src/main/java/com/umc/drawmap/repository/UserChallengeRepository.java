package com.umc.drawmap.repository;

import com.umc.drawmap.domain.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChallengeRepository extends JpaRepository<UserChallenge,Long>{}