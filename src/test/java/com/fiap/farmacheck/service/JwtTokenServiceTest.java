package com.fiap.farmacheck.service;

import com.fiap.farmacheck.exception.ValidateTokenErrorException;
import com.fiap.farmacheck.model.entity.Usuario;
import com.fiap.farmacheck.model.enums.TipoUsuario;
import com.fiap.farmacheck.security.service.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenServiceTest {

    private JwtTokenService jwtTokenService;

    @BeforeEach
    void setUp() throws Exception {
        jwtTokenService = new JwtTokenService();
        Field secretField = JwtTokenService.class.getDeclaredField("secret");
        secretField.setAccessible(true);
        secretField.set(jwtTokenService, "test-secret-key-for-unit-tests");
        jwtTokenService.getClass().getDeclaredMethod("init").invoke(jwtTokenService);
    }

    @Test
    void generateToken_deveRetornarTokenNaoNulo() {
        Usuario usuario = new Usuario("Admin", "admin@test.com", "password", TipoUsuario.ADMINISTRADOR);
        UserDetailsImpl userDetails = new UserDetailsImpl(usuario);

        String token = jwtTokenService.generateToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void getSubjectFromToken_comTokenValido_deveRetornarEmail() {
        Usuario usuario = new Usuario("Admin", "admin@test.com", "password", TipoUsuario.ADMINISTRADOR);
        UserDetailsImpl userDetails = new UserDetailsImpl(usuario);
        String token = jwtTokenService.generateToken(userDetails);

        String subject = jwtTokenService.getSubjectFromToken(token);

        assertEquals("admin@test.com", subject);
    }

    @Test
    void getSubjectFromToken_comTokenInvalido_deveLancarException() {
        assertThrows(ValidateTokenErrorException.class,
                () -> jwtTokenService.getSubjectFromToken("token-invalido"));
    }
}
