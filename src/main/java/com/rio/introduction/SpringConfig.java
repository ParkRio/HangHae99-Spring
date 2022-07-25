package com.rio.introduction;

import com.rio.introduction.repository.JpaPostRepository;
import com.rio.introduction.repository.PostRepository;
import com.rio.introduction.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
public class SpringConfig {

    @PersistenceContext
    EntityManager em;

    @Bean
    public PostService postService() {
        return new PostService(postRepository());
    }

    @Bean
    public PostRepository postRepository() {
        return new JpaPostRepository(em);
    }
}
