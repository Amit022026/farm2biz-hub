package com.farm2biz.security;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

//"OncePerRequestFilter" means: this code runs exactly once for EVERY
//single HTTP request that reaches our app, BEFORE it ever gets to a
//Controller. This is literally the bouncer standing at the front door.

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter{
	private final JwtUtil jwtUtil;
	private final CustomUserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request,
			@NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException, java.io.IOException {

		// Step 1: look for a header like: Authorization: Bearer eyJhbGci...
		String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			// No wristband shown at all -> let the request continue as
			// "anonymous". It'll only succeed later if the endpoint is
			// public (see SecurityConfig's permitAll() rules).
			filterChain.doFilter(request, response);
			return;
		}

		String token = authHeader.substring(7); // strip the "Bearer " prefix
		String email;

		try {
			email = jwtUtil.extractEmail(token);
		} catch (Exception e) {
			// token was malformed, tampered with, or expired -> treat as
			// not logged in, let Spring Security's normal rules reject it
			filterChain.doFilter(request, response);
			return;
		}

		// Step 2: if we found an email AND nobody has already authenticated
		// this request (avoids redoing work), look the user up and verify.
		if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(email);

			if (jwtUtil.isTokenValid(token, userDetails)) {
				// Step 3: mark this request as "logged in as this user" for
				// the rest of Spring to see - THIS is what makes
				// @PreAuthorize("hasRole('FARMER')") actually work later.
				UsernamePasswordAuthenticationToken authToken =
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}

		filterChain.doFilter(request, response); // hand off to the next step (eventually, the Controller)
	}
	
	
	
}
