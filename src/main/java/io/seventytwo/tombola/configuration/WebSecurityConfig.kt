package io.seventytwo.tombola.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Configuration
class WebSecurityConfig() {

    @Bean
    fun configure(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeRequests()
            .antMatchers("/css/**").permitAll()
            .antMatchers("/images/**").permitAll()
            .antMatchers("/actuator/health").permitAll()
            .antMatchers("/actuator/**").hasRole("USER")
            .antMatchers("/api/**").hasRole("USER")
            .antMatchers("/**").hasRole("USER")
            .anyRequest().authenticated()
            .and().formLogin().loginPage("/login").permitAll()

        return http.build()
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        val user = User.withDefaultPasswordEncoder()
            .username("user")
            .password("tombola")
            .roles("USER")
            .build()
        return InMemoryUserDetailsManager(user)
    }
}
