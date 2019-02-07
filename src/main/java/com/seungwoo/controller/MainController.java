package com.seungwoo.controller;

import com.seungwoo.domain.Account;
import com.seungwoo.dto.ParamDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Created by Leo.
 * User: ssw
 * Date: 2019-01-25
 * Time: 10:15
 */
@RestController
@RequiredArgsConstructor
public class MainController {

    private final ReactiveAuthenticationManager reactiveAuthenticationManager;

    private final ServerSecurityContextRepository contextRepository;

    /**
     * reactive security
     *
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
        return "redirect:index";
    }

    /**
     * 기존 security
     *
     * @return
     */
    @GetMapping("/front/all")
    @ResponseBody
    public String all() {
        System.out.println("===========all===========");
        return "all";
    }

    @GetMapping("/index")
    public String main(@ModelAttribute ParamDto paramDto) {
        System.out.println("===========index===========");
        System.out.println(paramDto.getTrId());
        //throw new RuntimeException();
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
    public String failure() {
        return "실패";
    }
}
