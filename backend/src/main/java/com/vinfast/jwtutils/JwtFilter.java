package com.vinfast.jwtutils;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;
	@Autowired
	private TokenManager tokenManager;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws ServletException, IOException {
	    String tokenHeader = request.getHeader("Authorization");
	    String username = null;
	    String token = null;

	    try {
	        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
	            token = tokenHeader.substring(7);
	            username = tokenManager.getUsernameFromToken(token);
	        } else {
	            System.out.println("Bearer String not found in token");
	        }

	        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
	            if (tokenManager.validateJwtToken(token, userDetails)) {
	                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
	                        userDetails, null, userDetails.getAuthorities());
	                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	            }
	        }

	        // Chuyển request sang filter tiếp theo
	        filterChain.doFilter(request, response);

	    } catch (ExpiredJwtException e) {
	        System.out.println("JWT Token has expired");

	        // Gửi phản hồi lỗi với thông báo token hết hạn
	        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Mã lỗi 401
	        response.setContentType("application/json");
	        response.getWriter().write("{\"message\": \"JWT Token has expired\"}");
	        return;
	    } catch (Exception e) {
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Mã lỗi 500
	        response.setContentType("application/json");
	        response.getWriter().write("{\"message\": \"An error occurred: " + e.getMessage() + "\"}");
	        return;
	    }
	}

}
