package com.sparta.week04.security;

import com.sparta.week04.model.Member;
import com.sparta.week04.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(MemberRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetails loadUserByUsername(String userNickname) throws UsernameNotFoundException {
        Member user = userRepository.findByName(userNickname)
                .orElseThrow(() -> new UsernameNotFoundException("Can't find " + userNickname));

        return new UserDetailsImpl(user);
    }
}
