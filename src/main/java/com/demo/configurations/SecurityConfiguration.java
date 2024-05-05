package com.demo.configurations;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.demo.services.AccountService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    @Autowired
    private AccountService usersService;

    
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.cors(cor -> cor.disable())
                .csrf(cs -> cs.disable())
                .authorizeHttpRequests(auth -> {
                    auth
                        .requestMatchers("/**",
                                "/admin/**",
                                "/assets/**",
                                "/home/**",
                                "/superadmin/**"
                                
                            ).permitAll()
                        .requestMatchers("/superadmin/account/**", "/superadmin/home/**").hasRole("SUPER_ADMIN")
                        .requestMatchers("/admin/**").hasAnyRole("SUPER_ADMIN", "ADMIN");
                        //.requestMatchers("/user/**").hasAnyRole("EMPLOYEE");
                })
                .formLogin(formLogin -> {
                    formLogin
                        .loginPage("/account/login")
                        .loginProcessingUrl("/account/process-login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(new AuthenticationSuccessHandler() {
                            @Override
                            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                                Authentication authentication) throws IOException, ServletException {
                                Collection<GrantedAuthority> roles = (Collection<GrantedAuthority>) authentication.getAuthorities();
                                Map<String, String> urls = new HashMap<>();
                                urls.put("ROLE_SUPER_ADMIN", "/superadmin/home/index");
                                urls.put("ROLE_ADMIN", "/admin/home");
                                urls.put("ROLE_EMPLOYEE", "/user/home/dashboard");

                                String url = "";
                                for (GrantedAuthority role : roles) {
                                    if (urls.containsKey(role.getAuthority())) {
                                        url = urls.get(role.getAuthority());
                                        break;
                                    }
                                }
                                System.out.println("url: " + url);
                                response.sendRedirect(url);
                            }
                        })
                        .failureUrl("/account/login?error")
                        .permitAll();
                })            
                .logout(logout -> {
                    logout
                        .logoutUrl("/account/logout")
                        .logoutSuccessUrl("/account/login?logout")
                        .permitAll();
                })
                .exceptionHandling(ex -> {
                    ex.accessDeniedPage("/account/accessDenied");
                })
                .build();
    }
	

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder) 
            throws Exception {
        builder.userDetailsService(usersService);
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}