package com.farm2biz.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.farm2biz.entities.User;

import lombok.RequiredArgsConstructor;

/* This class implements Spring Security's UserDetails interface OURSELVES,
instead of using Spring's generic built-in User class. The key
difference: we keep a reference to the REAL, full User entity inside
it - not just a stripped-down copy of two fields.

@RequiredArgsConstructor generates: public CustomUserDetailsImpl(User user) { this.user = user; }
*/

@RequiredArgsConstructor
public class CustomUserDetailsImpl implements UserDetails {

	private final User user; // the actual row from the "user" table - kept whole, not copied piece by piece
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		/*
		 * Returns a List containing one
		 *  - instance of SimpleGrantedAuthority(String roleName)
		 *  - SimpleGrantedAuthority implements GrantedAuthority
		 * This is the "permission label" Spring Security checks against
		 * hasRole("FARMER") in our @PreAuthorize annotations.
		 */
		return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
	}

	@Override
	public String getPassword() {

		return user.getPassword(); // the BCrypt hash, used only during the login comparison
	}

	@Override
	public String getUsername() {
		return user.getEmail(); // in our web app, "username" always means email
	}
	/* NOT part of the UserDetails interface - this is OUR escape hatch.
       Anywhere later in the app that needs the REAL user (userId, name,
	   etc.) - e.g. "which farmer is placing this request" - can call this 
	   instead of running a fresh database query. */
		public User getUser() {
			return user;
		}

		/* NOTE: isAccountNonExpired(), isAccountNonLocked(),
           isCredentialsNonExpired(), and isEnabled() are NOT overridden here.
		   The UserDetails interface itself provides default implementations
		    for all four that simply return true - fine for our app, since we
		   aren't (yet) building account-locking or expiry features.
	    */

}
