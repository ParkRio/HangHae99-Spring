package com.rio.intro.domain;

import com.rio.intro.domain.Board;
import com.rio.intro.dto.ResponseDto;
import com.rio.intro.dto.TimePlusResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// JpaRepositor<Entity 클래스, PK 타입>
// JpaRepository가 extends 되어 있을 경우 @Repository를 붙히지 않아도 자동으로 스프링이 빈에 레포지토리로 등록해준다.
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<TimePlusResponseDto> findAllByOrderByCreateAtDesc();

}
