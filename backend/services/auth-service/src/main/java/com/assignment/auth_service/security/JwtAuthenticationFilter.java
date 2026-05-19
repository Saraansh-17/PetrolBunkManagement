package com.assignment.auth_service.security;

import com.assignment.auth_service.jwt.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Reads the access JWT from an HttpOnly cookie, validates it, and populates {@link SecurityContextHolder}.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CookieTokenService cookieTokenService;
    private final JwtService jwtService;
    private final EmployeeUserDetailsService employeeUserDetailsService;

    public JwtAuthenticationFilter(
            CookieTokenService cookieTokenService,
            JwtService jwtService,
            EmployeeUserDetailsService employeeUserDetailsService) {
        this.cookieTokenService = cookieTokenService;
        this.jwtService = jwtService;
        this.employeeUserDetailsService = employeeUserDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        cookieTokenService.readAccessToken(request).ifPresent(token -> {
            Claims claims = jwtService.parseAndValidate(token);
            String employeeId = claims.getSubject();
            EmployeeUserDetails principal = employeeUserDetailsService.loadByEmployeeId(employeeId);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    principal,
                    null,
                    principal.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        });

        filterChain.doFilter(request, response);
    }
}
