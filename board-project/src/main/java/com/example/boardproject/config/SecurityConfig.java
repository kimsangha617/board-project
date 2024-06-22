package com.example.boardproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

//@EnableWebSecurity // SpringBoot 에서 SpringSecuriy 쓰게 되면 AutoConfig 안에 들어가 있기 때문에 안넣어도 됌
@Configuration
public class SecurityConfig  {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .formLogin().and()
                .build()
                ;
    }


}
