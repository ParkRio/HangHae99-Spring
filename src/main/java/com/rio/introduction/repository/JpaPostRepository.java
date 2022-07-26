package com.rio.introduction.repository;

import com.rio.introduction.domain.Post;
import com.rio.introduction.dto.ContentResponseDTO;
import com.rio.introduction.dto.CreateResponseDTO;

import javax.persistence.EntityManager;
import java.util.List;

public class JpaPostRepository implements PostRepository{

    // JPA는 Spring Data JPA 아님 --> EntityManager를 사용한다.
    private final EntityManager em;

    public JpaPostRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public CreateResponseDTO save(Post post) {
        em.persist(post);
        return new CreateResponseDTO(post.getTitle(), post.getWriter(), post.getContent());
    }

    @Override
    public List<ContentResponseDTO> findById(Long id) {
//        Post post = em.find(Post.class, id);
//        return Optional.ofNullable(post);
        List<ContentResponseDTO> result
                = em.createQuery("select new com.rio.introduction.dto.ContentResponseDTO(m.title, m.writer, m.content, m.createAt, m.modifiedAt) from Post as m where m.id = ?1", ContentResponseDTO.class)
                .setParameter(1, id)
                .getResultList();
        return result;
    }

    @Override
    public Post findByUpdateId(Long id) {
        Post post = em.find(Post.class, id);
        return post;
    }

    @Override
    public List<ContentResponseDTO> findAll() {
        List<ContentResponseDTO> result
                = em.createQuery("select new com.rio.introduction.dto.ContentResponseDTO(m.title, m.writer, m.content, m.createAt, m.modifiedAt) from Post as m order by m.createAt", ContentResponseDTO.class)
                .getResultList();
        return result;

//        return em.createQuery("select new com.rio.introduction.dto.CreateResponseDTO(m.title, m.writer, m.content) from Post as m", CreateResponseDTO.class)
//                .getResultList();
//        // Todo :: JPQL QUERY 공부... NEW (반환받을 클래스 설정 --> 패키지 경로 다 적야함 ,, CreateResponseDTO.class --> 반환할 타입 정확히 지정 )
//
//        return em.createQuery("select m from Post as m ORDER BY m.createAt desc ", Post.class)
//                .getResultList();
    }

    @Override
    public String checkPassword(Long id, String pwd) {

         Post post = em.find(Post.class,id);

//        List<Post> result = em.createQuery("select m from Post as m where m.id=?1", Post.class)
//                .setParameter(1, id)
//                .getResultList();

        if (pwd.equals(post.getPassword())) {
            return "success";
        }
        else return "false";
    }

    @Override
    public String deletePost(Long id) {
        Post post = em.find(Post.class, id);
        em.remove(post);
        return "success";
    }
}
