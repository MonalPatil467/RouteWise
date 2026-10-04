package RouteWise.Transportation.Configs;

import RouteWise.Transportation.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.jwtAuthenticationFilter =
                jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                // Disable CSRF because we are using JWT
                .csrf(csrf -> csrf.disable())

                // JWT authentication is stateless
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // =========================
                        // PUBLIC AUTHENTICATION APIs
                        // =========================
                        .requestMatchers(
                                "/api/user/signup",
                                "/api/user/login",
                                "/api/driver/signup",
                                "/api/driver/login"
                        ).permitAll()

                        // =========================
                        // SWAGGER
                        // =========================
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // =========================
                        // ADMIN APIs
                        // =========================
                        .requestMatchers(
                                "/api/admin/**"
                        ).hasRole("ADMIN")

                        // =========================
                        // DRIVER APIs
                        // =========================
                        .requestMatchers(
                                "/api/driver/**"
                        ).hasRole("DRIVER")

                        // =========================
                        // EVERYTHING ELSE
                        // JWT REQUIRED
                        // =========================
                        .anyRequest()
                        .authenticated()
                )

                // =========================
                // JWT FILTER
                // =========================
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
