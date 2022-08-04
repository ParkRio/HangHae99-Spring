package com.sparta.week04.repository;

import com.sparta.week04.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByName(String name);

    // userNickname 을 Login ID로 갖고 있기 때문에 findByUserNickname 와 중복 가입 방지를 위한 existsByUserNickname 만 추가
    boolean existsByName(String name);

}
