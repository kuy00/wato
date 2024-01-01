package com.wato.watobackend.repository;

import com.wato.watobackend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.status = 0 AND u.nickname = :nickname")
    Optional<User> findByNickname(String nickname);

    @Query("SELECT u FROM User u WHERE u.status = 0 AND u.email = :email")
    Optional<User> findByEmail(String email);

    Optional<User> findBySnsId(String snsId);

    @Query("SELECT u FROM User u WHERE u.status = 0 AND u.role = 0 ORDER BY u.id DESC")
    Page<User> getAll(PageRequest pageRequest);
}
