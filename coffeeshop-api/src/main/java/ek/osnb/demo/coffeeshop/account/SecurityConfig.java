package ek.osnb.demo.coffeeshop.account;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(CsrfConfigurer::spa)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error", "/actuator/health").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products/**", "/api/categories").permitAll()
                        .requestMatchers("/api/carts/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/orders/*/ship").hasRole("STAFF")
                        .requestMatchers("/api/staff/**").hasRole("STAFF")
                        .requestMatchers(HttpMethod.GET, "/api/orders/*").hasAnyRole("CUSTOMER", "STAFF")
                        .requestMatchers("/api/orders", "/api/orders/*/cancel", "/api/customers/me").hasRole("CUSTOMER")
                        .requestMatchers("/api/me", "/api/assistant/**").authenticated()
                        .anyRequest().denyAll()
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/api/login")
                        .successHandler((req,res,auth) -> res.setStatus(HttpServletResponse.SC_NO_CONTENT))
                        .failureHandler((req,res,ex) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED))

                )
                .logout(logout -> logout
                        .logoutUrl("/api/logout")
                        .logoutSuccessHandler((req,res,auth) -> res.setStatus(HttpServletResponse.SC_NO_CONTENT))

                )
                .exceptionHandling(eh -> eh
                        .authenticationEntryPoint((req, res, ex) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED))

                );
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
