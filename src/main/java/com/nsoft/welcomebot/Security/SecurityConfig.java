package com.nsoft.welcomebot.Security;

import com.nsoft.welcomebot.Security.AuthUtils.OauthRequestFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@ConditionalOnProperty(prefix = "app.security",
        name = "enabled",
        havingValue = "true")
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
                .antMatchers("/slack/events").permitAll()
                .antMatchers("/home.html").permitAll()
                .antMatchers("/api/v1/auth/login").permitAll()
                .antMatchers("/api/v1/users").hasRole("SUPERADMIN")
                .anyRequest().hasRole("ADMIN")
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(oauthRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * Make SUPERADMIN have all privileges of ADMIN
     */
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_SUPERADMIN > ROLE_ADMIN");
        return hierarchy;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(java.util.List.of("*"));
        configuration.setAllowedMethods(java.util.List.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowCredentials(false);
        configuration.setAllowedHeaders(java.util.List.of("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
