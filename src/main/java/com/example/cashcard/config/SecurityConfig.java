package com.example.cashcard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request.requestMatchers("/v1/cashcards/**")
                .hasRole("CARD-OWNER")).httpBasic(Customizer.withDefaults()).csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService testOnlyUsers(PasswordEncoder passwordEncoder){
        User.UserBuilder users = User.builder();
        UserDetails stan = users.username("stan").password(passwordEncoder.encode("stan@123")).roles("CARD-OWNER").build();
        UserDetails hank = users.username("hank").password(passwordEncoder.encode("hank@123")).roles("NON-OWNER").build();
        UserDetails lee = users.username("lee").password(passwordEncoder.encode("lee@123")).roles("CARD-OWNER").build();

        return new InMemoryUserDetailsManager(stan, hank, lee);
    }
}
