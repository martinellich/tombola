package io.seventytwo.tombola.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class WebSecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(c -> {
                    c.requestMatchers(
                            new AntPathRequestMatcher("/css/**"),
                            new AntPathRequestMatcher("/images/**"),
                            new AntPathRequestMatcher("/actuator/health")
                    ).permitAll();
                    c.requestMatchers(
                            new AntPathRequestMatcher("/actuator/**"),
                            new AntPathRequestMatcher("/api/**"),
                            new AntPathRequestMatcher("/**")
                    ).hasRole("USER");
                    c.anyRequest().authenticated();
                }
        );

        http.formLogin(c -> c.loginPage("/login").permitAll());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("tombola")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

}
