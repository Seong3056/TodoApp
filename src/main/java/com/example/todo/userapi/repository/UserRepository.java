package com.example.todo.userapi.repository;

import com.example.todo.userapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    //쿼리 메서드

    //이메일로 회원정보 조회
    Optional<User> findByEmail(String email);

    //이메일 중복 체크
//    @Query("Select Count(*) from User u Where u.email = ?1")
    boolean existsByEmail(String email);
}