package com.umc.drawmap.repository;


import com.umc.drawmap.domain.Challenge;
import com.umc.drawmap.domain.SpotImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpotImageRepository extends JpaRepository<SpotImage, Long> {

    Optional<SpotImage> findByIdAndChallenge(Long spotImageId, Challenge challenge);

    List<SpotImage> findAllByChallenge(Challenge challenge);

    void delete(SpotImage spotImage);
}