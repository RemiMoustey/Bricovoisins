package com.bricovoisins.clientui.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/login", "/accueil", "/utilisateurs").permitAll()
                .antMatchers("/my_conventions/**", "/demands", "/send_message/**/**").authenticated()
                .and()
                .formLogin()
                .defaultSuccessUrl("/home", true)
                .loginPage("/login")
                .permitAll();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/css/style.css", "/img/**", "/fonts/**", "/avatars/**");
        web.ignoring().antMatchers(HttpMethod.POST, "/validation");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new LibraryUserDetailsService());
    }
}
