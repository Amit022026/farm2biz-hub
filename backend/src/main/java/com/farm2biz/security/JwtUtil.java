package com.farm2biz.security;

import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


//This class's whole job: create a JWT when someone logs in successfully,
//and later read/verify a JWT when it comes back on a future request.
//Think of a JWT as a "signed, tamper-proof note" containing 3 parts,
//separated by dots, e.g.:  eyJhbGciOi....eyJzdWIiOi....SflKxwRJ
//1. HEADER    - what algorithm was used to sign it
//2. PAYLOAD   - the actual data (who this is, when it expires)
//3. SIGNATURE - proof it wasn't tampered with, using OUR secret key
//Anyone can READ parts 1 and 2 (they're just base64 text, not encrypted!),
//but only someone with our secret key could have produced a valid
//SIGNATURE - so we can trust a token is genuine if the signature checks out.

@Component
public class JwtUtil {
	
	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private long expirationMs;

	private SecretKey getSigningKey() {
		byte[] keyBytes = Base64.getDecoder().decode(secret);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	// Called ONCE, right after a successful login.
	
	public String generateToken(String email, String role) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + expirationMs);

		return Jwts.builder()
				.subject(email)              // "who is this token about"
				.claim("role", role)          // extra custom data we stuffed in
				.issuedAt(now)
				.expiration(expiry)
				.signWith(getSigningKey())
				.compact();                  // turns it into the final dotted string
	}

	// Called on EVERY future request that includes a token, to find out
	// who it claims to belong to.
	public String extractEmail(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private <T> T extractClaim(String token, Function<Claims, T> resolver) {
		Claims claims = Jwts.parser()
				.verifyWith(getSigningKey())   // this step FAILS loudly if the token was tampered with
				.build()
				.parseSignedClaims(token)
				.getPayload();
		return resolver.apply(claims);
	}

	// Called right before trusting a token: is it genuinely for this user,
	// and has it not expired yet?
	public boolean isTokenValid(String token, UserDetails userDetails) {
		String email = extractEmail(token);
		boolean notExpired = extractExpiration(token).after(new Date());
		return email.equals(userDetails.getUsername()) && notExpired;
	}

}
