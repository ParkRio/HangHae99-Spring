package com.sparta.week04.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sparta.week04.dto.PostRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
public class Post extends Timestamped{

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String author;

    @JsonIgnore // 속성(필드, 멤버변수) 수준에서 값 무시 --> 비밀번호 안나옴
    @Column(nullable = false)
    private String postPassword;

    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
    @OneToMany(mappedBy = "post")
    private List<Reply> replies = new ArrayList<>();

    public Post(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.author = requestDto.getAuthor();
        this.postPassword = requestDto.getPassword();
    }

    public void updatePost(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.author = requestDto.getAuthor();
        this.postPassword = requestDto.getPassword();
    }
}
