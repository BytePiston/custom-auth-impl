package com.cactus.springsecurity.client.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

	 private static final String[] WHITE_LIST_URLS = {"/api/v1/hello",
	 "/api/v1/user/register", "/api/v1/user/verifyRegistration*",
	 "/api/v1/user/resendVerificationToken*"};

//	private static final String[] WHITE_LIST_URLS = { "/api/v1/user/register", "/api/v1/user/verifyRegistration*",
//			"/api/v1/user/resendVerificationToken*" };

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(11);
	}

	@Bean
	public SecurityFilterChain webSecurityFilterChainClient(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.cors(CorsConfigurer::disable)
			.csrf(CsrfConfigurer::disable)
			.authorizeHttpRequests(req -> req.requestMatchers(WHITE_LIST_URLS).permitAll().anyRequest().authenticated())
			.oauth2Login(oauth2login -> oauth2login.loginPage("/oauth2/authorization/api-client-oidc"))
			.oauth2Client(Customizer.withDefaults());
		return httpSecurity.build();
	}

}
