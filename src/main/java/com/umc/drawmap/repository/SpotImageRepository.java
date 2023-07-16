package com.umc.drawmap.repository;


import com.umc.drawmap.domain.Challenge;
import com.umc.drawmap.domain.SpotImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotImageRepository extends JpaRepository<SpotImage, Long> {

    SpotImage findSpotImageByIdAndChallenge(Long spotImageId, Challenge challenge);
}