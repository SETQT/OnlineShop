package com.Shop.Security;

import java.util.Date;

import com.Shop.Security.Model.CustomUserDetail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenHelper {

	private final String JWT_SECRET = "HiringappForSEDSHCMUS";
	private String appName = "Hiring";

	private final long JWT_EXPIRATION = 604800000L;

	public String generateToken2(CustomUserDetail user) {
		Date now = new Date();
		Date expDate = new Date(now.getTime() + JWT_EXPIRATION);
		return Jwts.builder().setSubject(user.getUsername()).setIssuedAt(now).setExpiration(expDate)
				.signWith(SignatureAlgorithm.HS256, JWT_SECRET).compact();
	}

	public String generateToken(String subject) {
		long currentTimeMillis = System.currentTimeMillis();
		long expirationTimeMillis = currentTimeMillis + 3600 * 1000 * 4; // 1 hour -> 4 hour
		Date issueDate = new Date(currentTimeMillis);
		Date expirationDate = new Date(expirationTimeMillis);

		return Jwts.builder()
//	                .setClaims(claims)
				.setSubject(subject).setIssuedAt(issueDate).setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS256, JWT_SECRET).compact();
	}

	public Long getUserIdFromJWT(String token) {
		Claims claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();

		return Long.parseLong(claims.getSubject());
	}

	private Claims getAllClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}

	public String getUsernameFromToken(String token) {
		String username;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			username = claims.getSubject();
		} catch (Exception e) {
			username = null;
		}
		return username;
	}

	public boolean validateToken(String authToken) throws Exception {
		try {
			Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
			return true;
		} catch (MalformedJwtException ex) {

			throw new Exception("Invalid JWT token");
		} catch (ExpiredJwtException ex) {

			throw new Exception("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			throw new Exception("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {

			throw new Exception("JWT claims string is empty");
		}
//		return false;
	}

}
