package com.nsoft.welcomebot.Security.AuthUtils;


import com.nsoft.welcomebot.Services.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
    Every request goes through this filter. Check if the header token is valid, check if email from payload exists in database.
    If exists, assign UserDetails with role to UsernamePasswordAuthenticationToken
 */
@Configuration
public class OauthRequestFilter extends OncePerRequestFilter {

    private final UserService userService;


    public OauthRequestFilter(UserService userService) {
        this.userService = userService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String email = getEmailFromToken(request, response);
        setSecurityContext(request, response, email);
        filterChain.doFilter(request, response);
    }

    private String getEmailFromToken(HttpServletRequest request, HttpServletResponse response) {
        String token;
        String email = null;
        final String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            email = userService.getEmailFromToken(token);
        }
        return email;
    }

    private void setSecurityContext(HttpServletRequest request, HttpServletResponse response, String email) {
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails user = userService.loadUserByUsername(email);
                if (userService.validateUser(email) != null) {
                    var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            } catch (UsernameNotFoundException e) {
                response.setStatus(403);
            }
        }
    }
}
