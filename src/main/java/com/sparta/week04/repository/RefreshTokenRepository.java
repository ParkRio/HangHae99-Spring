package com.sparta.week04.repository;

import com.sparta.week04.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    // Memeber Id 값으로 토큰을 가져오기 위해 findByKey 만 추가
    Optional<RefreshToken> findByKey(String key);
}
