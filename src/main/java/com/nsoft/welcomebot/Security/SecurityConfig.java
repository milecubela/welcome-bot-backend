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

@Configuration
@ConditionalOnProperty(prefix = "app.security",
        name = "enabled",
        havingValue="true")
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
     * */
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_SUPERADMIN > ROLE_ADMIN");
        return hierarchy;
    }
}
