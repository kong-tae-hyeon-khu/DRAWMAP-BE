package com.umc.drawmap.repository;

import com.umc.drawmap.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
