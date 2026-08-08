package com.farm2biz.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class securityConfig {

	private final JwtAuthFilter jwtAuthFilter;

	@Value("${app.cors.allowed-origin}")
	private String allowedOrigin;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration cfg = new CorsConfiguration();
	    cfg.setAllowedOrigins(List.of(allowedOrigin));
	    cfg.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
	    cfg.setAllowedHeaders(List.of("Authorization", "Content-Type"));
	    cfg.setAllowCredentials(true);
	    UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
	    src.registerCorsConfiguration("/**", cfg);
	    return src;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		    .cors(cors -> cors.configurationSource(corsConfigurationSource()))
			// CSRF disabled: stateless JWT auth, no cookie-based sessions
			.csrf(csrf -> csrf.disable())

			// stateless: no server-side session, every request re-proves identity via JWT
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

			.authorizeHttpRequests(request -> request
					.requestMatchers("/users/register", "/users/login").permitAll()
					.requestMatchers(HttpMethod.GET, "/products/**").permitAll()
					.requestMatchers(HttpMethod.GET, "/categories/**").permitAll()
				.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
				
				.requestMatchers(HttpMethod.POST, "/products").hasRole("FARMER")
				.requestMatchers(HttpMethod.PUT, "/products/**").hasRole("FARMER")
				.requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("FARMER")

				.requestMatchers(HttpMethod.POST, "/categories").hasRole("ADMIN")
				.requestMatchers(HttpMethod.PUT, "/categories/**").hasRole("ADMIN")
				.requestMatchers(HttpMethod.DELETE, "/categories/**").hasRole("ADMIN")

				.requestMatchers(HttpMethod.POST, "/orders").hasRole("BULK_BUYER")
				.requestMatchers(HttpMethod.GET, "/orders/my-orders").hasRole("BULK_BUYER")
				.requestMatchers(HttpMethod.PATCH, "/orders/*/cancel").hasRole("BULK_BUYER")
				
				// ---- Bulk Buyer: Pay for an order ----
				.requestMatchers(HttpMethod.POST, "/payments").hasRole("BULK_BUYER")

				.requestMatchers(HttpMethod.GET, "/orders/farmer-orders").hasRole("FARMER")
				.requestMatchers(HttpMethod.PATCH, "/orders/*/accept").hasRole("FARMER")
				.requestMatchers(HttpMethod.PATCH, "/orders/*/reject").hasRole("FARMER")

				.requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
				.requestMatchers(HttpMethod.PUT, "/users/**").hasRole("ADMIN")
				.requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
				.requestMatchers(HttpMethod.GET, "/orders").hasRole("ADMIN")
				.requestMatchers("/reports/**").hasRole("ADMIN")
			
				.anyRequest().authenticated()
			)
						// run before Spring's auth filter so the SecurityContext is set each request
						.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		
			return http.build();
	}
}

