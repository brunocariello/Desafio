package br.com.loginneki.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(String defaultFilterProcessesUrl, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(defaultFilterProcessesUrl));
        this.jwtUtil = jwtUtil;
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String token = jwtUtil.getTokenFromRequest(request);

        // Não processar o token se o endpoint for "/api/login"
        if ("/api/login".equals(request.getRequestURI())) {
            // Retorna um UsernamePasswordAuthenticationToken vazio para não interromper o fluxo
            return new UsernamePasswordAuthenticationToken(null, null, new ArrayList<>());
        }

        if (token != null && jwtUtil.validateToken(token, jwtUtil.getUsernameFromToken(token))) {
            String username = jwtUtil.getUsernameFromToken(token);

            if (username != null) {
                return new UsernamePasswordAuthenticationToken(username, null, jwtUtil.getAuthoritiesFromToken(token));
            }
        }

        throw new AuthenticationException("Invalid token") {};
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
    }
}
