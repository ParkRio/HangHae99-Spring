package com.rio.introduction.repository;

import com.rio.introduction.domain.Post;

import java.util.*;

public class DisConnectDbPostRepository implements PostRepository{

    private static Map<Long, Post> store = new HashMap<>();
    private static long sequence = 0L; // save시 id값을 자동적으로 1++ 해주기 위함

    @Override
    public Post save(Post post) {post.setId(++sequence);
        post.setTitle("test를 위한 repository 입니다.");
        post.setWriter("rio");
        post.setContent("rio가 작성중인 글입니다.");
        store.put(post.getId(), post);
        return post;
    }

    @Override
    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(store.get(id)); // 그냥 store.get()으로 하면 값이 없을 경우을 대비해 Opational.ofNullable 해줘야함.
    }

    @Override
    public List<Post> findAll() {
        // inline 전
//        List<Post> list = new ArrayList<>(store.values()); // store에 저장된 값을 전부 가져온다.
//        return list;
        return new ArrayList<>(store.values());
    }
}
