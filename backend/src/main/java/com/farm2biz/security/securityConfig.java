package com.farm2biz.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

//@Configuration -> this class produces Spring beans
//@EnableWebSecurity -> enable Spring Security for this whole app
//@EnableMethodSecurity -> lets us write @PreAuthorize("hasRole('FARMER')")
//directly above Controller methods

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class securityConfig {

	private final CustomUserDetailsService userDetailsService;
	private final JwtAuthFilter jwtAuthFilter;

	// A "bean" turns plain-txt passwords into hashes,
	// can check the plain-text password match this hash or not
	// without need to un-hash anything.
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// Tells Spring Security: "when someone tries to log in, use OUR
	// CustomUserDetailsService to find them, and OUR PasswordEncoder to
	// check their password."
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	// UserServiceImpl.login() will inject this bean and call
	// .authenticate(email, password) on it directly.
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	// THE most important method in this file - the actual rulebook.
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			// CSRF protection is for browser-cookie-based sessions; our app
			// uses stateless JWT tokens instead, so we can safely disable it.
			.csrf(csrf -> csrf.disable())

			// STATELESS = "don't create/remember any server-side session for
			// a logged-in user." Every single request must prove who it is
			// all over again, using its JWT. This is what makes JWT auth
			// scale well - the server holds zero memory of who's logged in.
			.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

			.authorizeHttpRequests(auth -> auth
				// Anyone can register or log in without already being logged in
				.requestMatchers("/users/register", "/users/login").permitAll()
				// Anyone (even logged-out visitors) can BROWSE products -
				// matches "Buyer -> Search Product" not requiring login first
				.requestMatchers(HttpMethod.GET, "/products/**").permitAll()
				// Swagger UI / OpenAPI docs — no auth required
				.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
				// every other endpoint requires a valid JWT
				.anyRequest().authenticated()
			)
			.authenticationProvider(authenticationProvider())
			// Insert OUR bouncer (Step 5) to run BEFORE Spring Security's
			// own built-in login filter, on every request.
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}

