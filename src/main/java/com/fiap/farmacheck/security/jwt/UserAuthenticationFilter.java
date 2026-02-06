package com.fiap.farmacheck.security.jwt;

import com.fiap.farmacheck.model.entity.Usuario;
import com.fiap.farmacheck.repository.UsuarioRepository;
import com.fiap.farmacheck.security.service.UserDetailsImpl;
import com.fiap.farmacheck.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;

    private final UsuarioRepository usuarioRepository;

    public UserAuthenticationFilter(JwtTokenService jwtTokenService, UsuarioRepository usuarioRepository) {
        this.jwtTokenService = jwtTokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/v3/api-docs") ||
               path.startsWith("/swagger-ui") ||
               path.startsWith("/swagger-resources") ||
               path.startsWith("/webjars");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = recoveryToken(request);

        if (token != null) {
            String subject = jwtTokenService.getSubjectFromToken(token);

            if (subject != null) {
                Usuario usuario = usuarioRepository.findByEmail(subject).orElse(null);

                if (usuario != null) {
                    UserDetailsImpl userDetails = new UserDetailsImpl(usuario);
                    Authentication authentication =
                            new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recoveryToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}