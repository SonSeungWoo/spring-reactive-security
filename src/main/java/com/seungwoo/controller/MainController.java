package com.seungwoo.controller;

import com.seungwoo.domain.Account;
import com.seungwoo.service.AccountService;
import com.seungwoo.service.AccountServiceImpl;
import com.seungwoo.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Leo.
 * User: ssw
 * Date: 2019-01-25
 * Time: 10:15
 */
@Controller
public class MainController {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountServiceImpl accountServiceImpl;

    @Autowired
    TestService testService;

    @GetMapping("/login")
    public String login(@ModelAttribute Account account){
        Mono<UserDetails> userDetails = accountServiceImpl.findByUsername(account.getUsername());
        System.out.println(userDetails.toString());
        Authentication authentication = new UsernamePasswordAuthenticationToken("seungwoo1","0000", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        ReactiveSecurityContextHolder.getContext()
                .switchIfEmpty(Mono.error(new IllegalStateException("ReactiveSecurityContext is empty")))
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getName)
                .subscriberContext(ReactiveSecurityContextHolder.withAuthentication(authentication));
        return "index";
    }

    /*@GetMapping("/login")
    public String login(@ModelAttribute Account account, HttpServletRequest request){
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("seungwoo1", "0000", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));

        UserDetails ckUserDetails = testService.loadUserByUsername("seungwoo1");
        Authentication authentication = new UsernamePasswordAuthenticationToken("seungwoo1", ckUserDetails.getPassword(), ckUserDetails.getAuthorities());

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

        return "index";
    }*/

    @GetMapping("/")
    public String main(){
        List<Account> list = accountService.findAll();
        System.out.println("===========index===========");
        return "index";
    }

    @GetMapping("/home")
    public String home(){
        System.out.println("===========home===========");
        return "home";
    }

    @GetMapping("/fail")
    public String fail(){
        System.out.println("===========fail===========");
        return "fail";
    }
}
