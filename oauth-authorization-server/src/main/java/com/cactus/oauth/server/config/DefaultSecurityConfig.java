package com.cactus.oauth.server.config;

import com.cactus.oauth.server.service.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class DefaultSecurityConfig {

	@Autowired
	private CustomAuthenticationProvider customAuthenticationProvider;

	private static final String[] WHITE_LIST_URLS = { "/login/**", "/oauth2/**", "/eureka/**" };

	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeRequests(authorizeRequests -> authorizeRequests.requestMatchers(WHITE_LIST_URLS)
			.permitAll()
			.requestMatchers(WHITE_LIST_URLS)
			.permitAll()
			.anyRequest()
			.authenticated()).formLogin(Customizer.withDefaults());
		return http.build();
	}

	@Autowired
	public void bindAuthenticationProvider(AuthenticationManagerBuilder authenticationManagerBuilder) {
		authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider);
	}

}
