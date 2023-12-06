package com.Shop.Security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.Shop.Security.Model.UserDetailServiceImp;
import com.fasterxml.jackson.databind.ObjectMapper;


import io.jsonwebtoken.ExpiredJwtException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenHelper tokenProvider;

	@Autowired
	private UserDetailServiceImp customUserDetailsService;

	@Autowired
	UserDetailServiceImp userDetailimp;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			// Lấy jwt từ request
			String jwt = getJwtFromRequest(request);

			if (jwt == null) {
				filterChain.doFilter(request, response);
				return;
			}
//				System.out.println("-----"+jwt);
				if (tokenProvider.validateToken(jwt)) {
					// Lấy user từ chuỗi jwt
					String userId = tokenProvider.getUsernameFromToken(jwt);
					UserDetails userDetails = userDetailimp.loadUserByUsername(userId);

					if (userDetails != null) {
						UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
								// **********///
								userDetails, null, userDetails.getAuthorities());
//					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

						SecurityContextHolder.getContext().setAuthentication(authentication);
					}
				}

				filterChain.doFilter(request, response);
		} catch (Exception ex) {
			logger.error("failed on set user authentication", ex);

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json;charset=UTF-8");

		        Map<String, Object> error = new HashMap<>();
		        error.put("status", "failed");
		        error.put("message",ex.getMessage());

		        ObjectMapper mapper = new ObjectMapper();
		        mapper.writeValue(response.getOutputStream(), error);
//		        return;
		} 
			

	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = ((jakarta.servlet.http.HttpServletRequest) request).getHeader("Authorization");
//		System.out.println(bearerToken);
		// Kiểm tra xem header Authorization có chứa thông tin jwt không
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {

			return bearerToken.substring(7);
		}
		return null;
	}

}
