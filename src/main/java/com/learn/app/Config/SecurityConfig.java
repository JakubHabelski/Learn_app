package com.learn.app.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserService  userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf().ignoringRequestMatchers("/h2-console/**", "/adminH2/**").disable()
                .headers(
                        headers -> headers
                                .frameOptions(frameOptions -> frameOptions.disable())
                )
                .authorizeRequests(authorize -> authorize
                        .requestMatchers( "/getResponse","/checkPassword","/ForgotPass","/ResetPass","/reset_pass/{UserLogin}",
                                "/ChangePass", "/checkMail","/checkLogin","show_image","/login_style.css",
                                "/loginform","/PostRegister", "/login_logic" , "/GetRegister", "/login" ,
                                "/static/**", "/login_style.css" ,"/stylesheet.css" ,
                                "/register_style.css" , "/register_success/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/loginform")
                        .loginProcessingUrl("/login")
                        .failureUrl("/loginform_error")
                        .usernameParameter("UserLogin")
                        .passwordParameter("UserPass")
                        .defaultSuccessUrl("/userpanel", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/loginform")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedPage("/access-denied")
                )
                .build();
    }



}