package com.seungwoo.service;

import com.seungwoo.domain.Account;
import com.seungwoo.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by Leo.
 * User: ssw
 * Date: 2019-01-22
 * Time: 17:28
 */
@Service("reactiveAccountService")
@Transactional
@RequiredArgsConstructor
public class AccountService implements ReactiveUserDetailsService {

    private final AccountMapper accountMapper;

    @Override
    public Mono<UserDetails> findByUsername(String userId) {
        return Mono.just(accountMapper.findByName(userId))
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.error(new BadCredentialsException(String.format("User %s not found in database", userId))))
                .map(this::createSpringSecurityUser);
    }

    private User createSpringSecurityUser(Account account) {
        /*List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority("ROLE_ADMIN"))
                .collect(Collectors.toList());*/
        return new User(account.getUsername(), account.getPassword(), getAuthority());
    }

    private List<GrantedAuthority> getAuthority() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

}
