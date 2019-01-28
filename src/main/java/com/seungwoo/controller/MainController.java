package com.seungwoo.controller;

import com.seungwoo.domain.Account;
import com.seungwoo.service.AccountService;
import com.seungwoo.service.AccountServiceImpl;
import com.seungwoo.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

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

    @Autowired
    private ReactiveAuthenticationManager reactiveAuthenticationManager;

    @Autowired
    private ServerSecurityContextRepository contextRepository;

    /**
     * reactive security
     * @param account
     * @param serverWebExchange
     * @return
     */
    @GetMapping("/front")
    public String login(@ModelAttribute Account account, ServerWebExchange serverWebExchange) {
        reactiveAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(account.getUsername(), null, null))
                .subscribe(authentication -> {
                   SecurityContextImpl securityContext = new SecurityContextImpl();
                   securityContext.setAuthentication(authentication);
                   this.contextRepository.save(serverWebExchange, securityContext)
                           .subscriberContext(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)))
                           .subscribe();
                });
        /*Mono<UserDetails> userDetails = accountServiceImpl.findByUsername(account.getUsername());
        System.out.println(userDetails.toString());
        Authentication authentication = new UsernamePasswordAuthenticationToken("seungwoo1","0000", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        ReactiveSecurityContextHolder.getContext()
                .switchIfEmpty(Mono.error(new IllegalStateException("ReactiveSecurityContext is empty")))
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getName)
                .subscriberContext(ReactiveSecurityContextHolder.withAuthentication(authentication));*/
        return "redirect:index";
    }

    /**
     * 기존 security
     * @return
     */
    /*@GetMapping("/front")
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

    @GetMapping("/front/all")
    @ResponseBody
    public String all() {
        System.out.println("===========all===========");
        return "all";
    }

    @GetMapping("/index")
    public String main() {
        List<Account> list = accountService.findAll();
        System.out.println("===========index===========");
        return "index";
    }

    @GetMapping("/home")
    public String home() {
        System.out.println("===========home===========");
        return "home";
    }

    @GetMapping("/fail")
    @ResponseBody
    public String fail() {
        System.out.println("===========fail===========");
        return "fail";
    }

    @GetMapping("/success")
    @ResponseBody
    public String success() {
        System.out.println("===========fail===========");
        return "success";
    }

    @GetMapping("/authentication-failure")
    @ResponseBody
    public String failure(){
        return "실패";
    }
}
