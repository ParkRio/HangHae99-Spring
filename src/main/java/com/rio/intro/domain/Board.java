package com.rio.intro.domain;

import com.rio.intro.dto.RequestDto;
import com.rio.intro.dto.ResponseDto;
import com.rio.intro.dto.TimePlusResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

// Todo :: domain이란 게시글, 댓글, 회원, 정산, 결제 등 소프트웨어에 대한 요구사항 혹은 문제영역
// Todo :: Entity 클래스에서는 절대 @Setter 어노테이션을 사용하지 않는다.

@Getter
@NoArgsConstructor
@Entity // database의 테이블과 매칭될 클래스임을 나타냄
public class Board extends Timestamped{

    @CreatedDate
    private LocalDateTime createAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 스프링부트 2.0 이상부터는 IDENTITY 옵션을 추가해야만 id값이 자동으로 증가함.
    private Long id; // Mysql 기준으로 Long 타입으로 선언하면 알아서 Mysql의 bigint 타입이 된다. 왠만하면 PK키는 Long 타입 추천

    @Column(nullable = false) // @Column 테이블의 칼럼을 나타내며 굳이 선언하지 않더라도 해당 클래스의 필드는 모두 칼럼이 된다.
    private String title;

    @Column(nullable = false) // @Column을 굳이 사용하는 이유는 기본값 외에 추가로 변경이 필요한 옵션이 있으면 사용한다.
    private String author;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String password;

    @Builder
    public Board(String title, String author, String content, String password) {
        this.title = title;
        this.author = author;
        this.content = content;
        this.password = password;
    }

    public ResponseDto responseSaveData() {
        return ResponseDto.builder()
                .title(title)
                .author(author)
                .content(content)
                .build();
    }

    public TimePlusResponseDto responseUpdateData() {
        return TimePlusResponseDto.builder()
                .title(title)
                .author(author)
                .content(content)
                .build();
    }

    public void update(RequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.author = requestDto.getAuthor();
        this.content = requestDto.getContent();
        this.password = requestDto.getPassword();
    }
}
