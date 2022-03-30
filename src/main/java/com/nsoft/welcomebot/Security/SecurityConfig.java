package com.nsoft.welcomebot.Security;

import com.nsoft.welcomebot.Security.AuthUtils.OauthRequestFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final OauthRequestFilter oauthRequestFilter;

    public SecurityConfig(OauthRequestFilter oauthRequestFilter) {
        this.oauthRequestFilter = oauthRequestFilter;
    }

    /*
    Request ADMIN role on every route
    Filter oauthRequestFilter checks if user is ADMIN
     */

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/home.html").permitAll()
                .antMatchers("/api/v1/auth/login").permitAll()
//                .anyRequest().hasRole("ADMIN")
                .anyRequest().permitAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(oauthRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
