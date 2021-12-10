package com.quangnv.uet.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.quangnv.uet.jwt.JwtTokenProvider;
import com.quangnv.uet.service.impl.UserServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private UserServiceImpl userServiceImpl;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String jwt = getJwtFromRequest(request);
		try {
			if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
				String userId = jwtTokenProvider.getUserIdFromJWT(jwt);

				UserDetails userDetails = userServiceImpl.loadUserByUsername(userId);

				if (userDetails != null) {
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
			}else {
				SecurityContextHolder.getContext().setAuthentication(null);
			}
		} catch (Exception e) {
			log.error("failed on set user authentication", e);
		}
		filterChain.doFilter(request, response);
	}

	public String getJwtFromRequest(HttpServletRequest request) {
		String jwt = request.getHeader("Authorization");
		if (StringUtils.hasText(jwt) && jwt.startsWith("Bearer ")) {
			return jwt.substring(7);
		}
		return null;
	}
}
