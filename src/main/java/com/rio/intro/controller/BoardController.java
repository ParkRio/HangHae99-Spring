package com.rio.intro.controller;

import com.rio.intro.dto.RequestDto;
import com.rio.intro.dto.ResponseDto;
import com.rio.intro.dto.TimePlusResponseDto;
import com.rio.intro.service.BoardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController // 컨트롤러를 JSON으로 반환하는 컨트롤러로 만들어 준다.
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // Todo 게시글 생성
    @PostMapping("/api/create-board")
    public ResponseDto searchAllBoard(@RequestBody RequestDto requestDto) {
        return boardService.save(requestDto);
    }

    // Todo 게시글 변경
    @PutMapping("/api/update-board/{id}")
    public Optional<TimePlusResponseDto> updateMyBoard(@PathVariable Long id, @RequestBody RequestDto requestDto) {
        return boardService.update(id, requestDto);
    }

    // Todo 게시글 id로 찾기
    @GetMapping("/api/search-board/{id}")
    public Optional<TimePlusResponseDto> searchMyBoard(@PathVariable Long id) {
        return boardService.search(id);
    }

    // Todo 모든 게시글 찾기
    @GetMapping("/api/search-all-board")
    public List<TimePlusResponseDto> searchAllBoard() {
        return boardService.searchAll();
    }

    // Todo 패스워드 검사
    @PostMapping("/api/check-password/{id}")
    public String checkPassword(@PathVariable Long id, @RequestParam String password) {
        return boardService.checkPassword(id, password);
    }

    // Todo 게시글 삭제
    @DeleteMapping("/api/delete-board/{id}")
    public String deleteBoard(@PathVariable Long id) {
        return boardService.deleteBoard(id);
    }
}
