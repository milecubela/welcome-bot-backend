package com.nsoft.welcomebot.Security.AuthUtils;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.gson.JsonObject;
import com.nsoft.welcomebot.Services.OauthTokenService;
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
import java.security.GeneralSecurityException;


/*
    Every request goes through this filter. Check if the header token is valid, check if email from payload exists in database.
    If exists, assign UserDetails with role to UsernamePasswordAuthenticationToken
 */
@Configuration
public class OauthRequestFilter extends OncePerRequestFilter {


    //private final GoogleIdTokenVerifier verifier;
    private final UserService userService;
    private final OauthTokenService oauthTokenService;


    public OauthRequestFilter(GoogleIdTokenVerifier verifier, UserService userService, OauthTokenService oauthTokenService) {
        //this.verifier = verifier;
        this.userService = userService;
        this.oauthTokenService = oauthTokenService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");


        String email = null;
        String token = null;


        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            token = authorizationHeader.substring(7);
            try {
                JsonObject jsonObject = oauthTokenService.verifyGoogleToken(token);
                System.out.println(jsonObject.get("email"));
//                var email2 = jsonObject.get("email").toString();
                email = jsonObject.get("email").getAsString();
                System.out.println(email);
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
//                GoogleIdToken googleIdToken = verifier.verify(token);
//                GoogleIdToken.Payload payload = googleIdToken.getPayload();
//                email = payload.getEmail();
        }

        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails user = userService.loadUserByUsername(email);
                System.out.println(user);
                if (userService.validateUser(email)) {
                    var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            user.getAuthorities()
                    );

                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext()
                            .setAuthentication(usernamePasswordAuthenticationToken);
                }
            } catch (UsernameNotFoundException e) {
// todo
                System.out.println("everything failed");
            }
        }
        filterChain.doFilter(request, response);
    }
}
