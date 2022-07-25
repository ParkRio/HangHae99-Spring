package com.rio.introduction.domain;

import com.rio.introduction.dto.PostDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor // default construtor 생성
public class Post {
    /**
     * encapsulation
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id

    @Column(nullable = false)
    private String title; // 제목

    @Column(nullable = false)
    private String writer; // 작성자

    @Column(nullable = false)
    private String content; // 내용

    @Column(nullable = false)
    private String password; // 비밀번호

    public Post(PostDTO dto) {
        this.title = dto.getTitle();
        this.writer = dto.getWriter();
        this.content = dto.getContent();
        this.password = dto.getPassword();
    }
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getWriter() {
//        return writer;
//    }
//
//    public void setWriter(String writer) {
//        this.writer = writer;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
}
