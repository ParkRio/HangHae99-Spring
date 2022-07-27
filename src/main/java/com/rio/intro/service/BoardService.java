package com.rio.intro.service;

import com.rio.intro.domain.Board;
import com.rio.intro.domain.BoardRepository;
import com.rio.intro.dto.RequestDto;
import com.rio.intro.dto.ResponseDto;
import com.rio.intro.dto.TimePlusResponseDto;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    // Todo :: 게시글 저장
    @Transactional
    public ResponseDto save(RequestDto requestDto) {
        Board board = boardRepository.save(requestDto.save());
        return board.responseSaveData();
    }

    // Todo :: 게시글 변경 / 수정
    @Transactional
    public Optional<TimePlusResponseDto> update(Long id, RequestDto requestDto) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));
        board.update(requestDto);
        //return board.responseUpdateData();

//        return Optional.of(new TimePlusResponseDto(
//                boardRepository.findById(id).get().getTitle(),
//                boardRepository.findById(id).get().getAuthor(),
//                boardRepository.findById(id).get().getContent(),
//                boardRepository.findById(id).get().getCreateAt(),
//                boardRepository.findById(id).get().getModifiedAt()
//        ));

        return Optional.of(new TimePlusResponseDto(
                board.getTitle(),
                board.getAuthor(),
                board.getContent(),
                board.getCreateAt(),
                board.getModifiedAt()
        ));
    }

    // Todo :: id 조회
    public Optional<TimePlusResponseDto> search(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));

        return Optional.of(new TimePlusResponseDto(
                boardRepository.findById(id).get().getTitle(),
                boardRepository.findById(id).get().getAuthor(),
                boardRepository.findById(id).get().getContent(),
                boardRepository.findById(id).get().getCreateAt(),
                boardRepository.findById(id).get().getModifiedAt()
        ));
    }

    // Todo :: 전체 조회
    public List<TimePlusResponseDto> searchAll() {
        return boardRepository.findAllByOrderByCreateAtDesc();
    }

    public String checkPassword(Long id, String password) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));
        String result = board.getPassword();
        if (password.equals(result)) {
            return "success";
        } else return "false";
    }

    public String deleteBoard(Long id) {
        try {
            boardRepository.deleteById(id);
            return "success";
        }
        catch (UnexpectedRollbackException | EmptyResultDataAccessException e) {
            return "null";
        }
    }
}
