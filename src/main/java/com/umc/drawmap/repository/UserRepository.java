package com.umc.drawmap.repository;

import com.umc.drawmap.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByNickName(String nickName);
    List<User> findAll();

    Optional<User> findByEmail(String email);

    long count();


    Boolean existsByEmail(String email);
}
