package com.xd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.concurrent.locks.ReentrantLock;

@SpringBootApplication
@RestController
public class App 
{
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @GetMapping("/hello")
    public String hello(Principal principal) {
        return "Hello 2, " + principal.getName();
    }

    @GetMapping("/callback")
    public String callback(Principal principal) {
        return "Hello 2, " + principal.getName();
    }

    @GetMapping("/")
    public String hello(@AuthenticationPrincipal OidcUser user) {
        return "Hello 2, " + user.getFullName();
    }
}
