package com.fiap.farmacheck.security.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final UserAuthenticationFilter userAuthenticationFilter;

    public SecurityConfiguration(UserAuthenticationFilter userAuthenticationFilter) {
        this.userAuthenticationFilter = userAuthenticationFilter;
    }

    // Endpoints que não requerem autenticação para serem acessados
    public static final String [] ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED = {
            "/",
            "/api/auth/sign-in",
            "/api/auth/sign-up",
            "/h2-console/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**"
    };

    // Endpoints que requerem autenticação para serem acessados
    public static final String [] ENDPOINTS_WITH_AUTHENTICATION_REQUIRED = {
            "/api/test",
            "/api/disponibilidade",
            "/api/estoques",
            "/api/estoques/{id}",
            "/api/unidades",
            "/api/unidades/{id}",
            "/api/movimentacoes",
            "/api/movimentacoes/{id}",
    };

    // Endpoints que só podem ser acessador por usuários com permissão de "administrador(a)"
    public static final String [] ENDPOINTS_ADMINISTRADOR = {
            "/api/test/administrador",
            "/api/medicamentos",
            "/api/medicamentos/{id}",
            "/api/estoques",
            "/api/estoques/{id}",
            "/api/unidades",
            "/api/unidades/{id}",
            "/api/movimentacoes",
            "/api/movimentacoes/{id}",
    };

    // Endpoints que só podem ser acessador por usuários com permissão de "paciente"
    public static final String [] ENDPOINTS_PACIENTE = {
            "/api/test/paciente",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).permitAll()
                    .requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_REQUIRED).authenticated()
                    .requestMatchers(ENDPOINTS_ADMINISTRADOR).hasAuthority("ADMINISTRADOR")
                    .requestMatchers(ENDPOINTS_PACIENTE).hasAuthority("PACIENTE")
                    .anyRequest().authenticated())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .addFilterBefore(userAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
