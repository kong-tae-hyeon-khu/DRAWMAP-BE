package com.umc.drawmap.repository;

import com.umc.drawmap.domain.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotRepository extends JpaRepository<Spot, Long> {}