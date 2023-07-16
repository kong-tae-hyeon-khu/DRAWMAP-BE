package com.umc.drawmap.repository;

import com.umc.drawmap.domain.Challenge;
import com.umc.drawmap.domain.Scrap;
import com.umc.drawmap.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapRepository extends JpaRepository<Scrap, Long>{

    Boolean existsScrapByUserAndChallenge(User user, Challenge challenge);
}