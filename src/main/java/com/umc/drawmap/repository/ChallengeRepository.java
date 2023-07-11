package com.umc.drawmap.repository;

import com.umc.drawmap.domain.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge, Long>{
    Optional<Challenge> findById(Long challengeId);
}
