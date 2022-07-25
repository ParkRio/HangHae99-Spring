package com.rio.introduction.repository;

import com.rio.introduction.domain.Post;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class JpaPostRepository implements PostRepository{

    // JPA는 Spring Data JPA 아님 --> EntityManager를 사용한다.
    private final EntityManager em;

    public JpaPostRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Post save(Post post) {
        em.persist(post);
        return post;
    }

    @Override
    public Optional<Post> findById(Long id) {
        Post post = em.find(Post.class, id);
        return Optional.ofNullable(post);
    }

    @Override
    public List<Post> findAll() {
//        List<Post> result = em.createQuery("select m from Post as m", Post.class)
//                .getResultList();
//        return result;
        return em.createQuery("select m from Post as m ORDER BY m.id desc ", Post.class)
                .getResultList();
    }
}
