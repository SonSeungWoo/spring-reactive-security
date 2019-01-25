package com.seungwoo.service;

import com.seungwoo.domain.Account;
import com.seungwoo.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Leo.
 * User: ssw
 * Date: 2019-01-25
 * Time: 16:55
 */
@Service
@Transactional
@RequiredArgsConstructor
public class TestService implements UserDetailsService {

    private final AccountMapper accountMapper;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Account account = accountMapper.findByName(userId);
        if(account == null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new User(account.getUsername(), account.getPassword(), getAuthority());
    }

    private List<SimpleGrantedAuthority> getAuthority() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}
