package com.rio.intro.repository;

import com.rio.intro.domain.Board;
import com.rio.intro.domain.BoardRepository;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class BoardRepositoryTest {

    @Autowired
    BoardRepository boardRepository;

    @After
    public void cleanUp() {
        boardRepository.deleteAll();
    }

    @Test
    public void 게시글불러오기() {
        // given
        String title = "볼따구";
        String author = "Rio";
        String content = "철부지 앙꼬 볼따구";
        String password = "1234";

        // boardRepository.save() --> 테이블 board의 insert/update 쿼리를 실행한다.
        boardRepository.save(Board.builder()
                .title(title)
                .author(author)
                .content(content)
                .password(password)
                .build());
        // when
        List<Board> boardList = boardRepository.findAll();
        // then
        Board board = boardList.get(0);
        assertThat(board.getTitle()).isEqualTo(title);
        assertThat(board.getContent()).isEqualTo(content);
        assertThat(board.getAuthor()).isEqualTo(author);
        assertThat(board.getPassword()).isEqualTo(password);
    }
}