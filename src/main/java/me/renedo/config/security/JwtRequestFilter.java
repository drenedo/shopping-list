package me.renedo.config.security;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.renedo.config.security.util.TokenValidator;
import me.renedo.shared.Component;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final static String HEADER_START = "Bearer ";
    private final UserDetailsService userDetailsService;
    private final TokenValidator tokenValidator;

    public JwtRequestFilter(UserDetailsService userDetailsService, TokenValidator tokenValidator) {
        this.userDetailsService = userDetailsService;
        this.tokenValidator = tokenValidator;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String header = getHeader(request);
        String username = getUserName(header);
        if(username != null){
            login(request, username);
        }
        chain.doFilter(request, response);
    }

    private String getHeader(HttpServletRequest request){
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        return  (header == null || !header.startsWith(HEADER_START)) ? null : header;
    }

    private String getUserName(String header){
        return header == null ? null : tokenValidator.validateTokenAndGetUsername(header.substring(HEADER_START.length()));
    }

    // set user on spring security
    private void login(HttpServletRequest request, String username){
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
