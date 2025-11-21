package ch.martinelli.oss.tombola.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) {
		http.authorizeHttpRequests(c -> {
			c.requestMatchers("/css/**", "/images/**", "/actuator/health").permitAll();
			c.anyRequest().authenticated();
		});

		http.formLogin(c -> c.loginPage("/login").permitAll());

		return http.build();
	}

	@SuppressWarnings({ "java:S6437", "deprecation" })
	@Bean
	public UserDetailsService userDetailsService() {
		var user = User.withDefaultPasswordEncoder().username("user").password("tombola").roles("USER").build();
		return new InMemoryUserDetailsManager(user);
	}

}
