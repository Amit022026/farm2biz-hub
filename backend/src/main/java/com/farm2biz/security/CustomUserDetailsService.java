package com.farm2biz.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.farm2biz.entities.User;
import com.farm2biz.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("No user found with email: " + email));

		// CHANGED: instead of building Spring Security's generic, built-in
		// UserDetails (which only holds a username/password/authorities),
		// we wrap OUR real User entity in CustomUserDetailsImpl. This means
		// anything downstream (Controllers, other Services) that gets hold
		// of the logged-in principal can pull out the FULL user record -
		// userId, name, everything - with zero extra database queries.
		return new CustomUserDetailsImpl(user);
	}
	
}
