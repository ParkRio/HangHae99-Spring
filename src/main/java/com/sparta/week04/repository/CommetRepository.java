package com.sparta.week04.repository;

import com.sparta.week04.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommetRepository extends JpaRepository<Reply, Long> {
}
