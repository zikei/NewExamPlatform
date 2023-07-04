package com.example.examPlatform.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	@Autowired
    UserDetailsService userDetailsService;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(authz -> authz
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
			)
			.authorizeHttpRequests(authz -> authz
					.requestMatchers("/img/**").permitAll()
				)
			.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/ExamPlatform/","/ExamPlatform/Home","/ExamPlatform/Account/Entry").permitAll()
				.anyRequest().authenticated()
			)
			.formLogin((form) -> form
				.loginPage("/ExamPlatform/Login")
				.defaultSuccessUrl("/ExamPlatform/Mypage")
				.permitAll()
			)
			.logout((logout) -> logout
				.logoutUrl("/ExamPlatform/Logout")
				.logoutSuccessUrl("/ExamPlatform/Home")
				.permitAll());

		return http.build();
	}
	
	@Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
