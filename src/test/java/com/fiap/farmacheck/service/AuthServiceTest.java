package com.fiap.farmacheck.service;

import com.fiap.farmacheck.mapper.UsuarioMapper;
import com.fiap.farmacheck.model.dto.usuario.UsuarioRequestDTO;
import com.fiap.farmacheck.model.entity.Usuario;
import com.fiap.farmacheck.model.enums.TipoUsuario;
import com.fiap.farmacheck.repository.UsuarioRepository;
import com.fiap.farmacheck.security.service.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private AuthService authService;

    @Test
    void loadUserByUsername_quandoUsuarioExiste_deveRetornarUserDetails() {
        Usuario usuario = new Usuario("Admin", "admin@test.com", "encoded", TipoUsuario.ADMINISTRADOR);

        when(usuarioRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(usuario));

        UserDetails result = authService.loadUserByUsername("admin@test.com");

        assertNotNull(result);
        assertEquals("admin@test.com", result.getUsername());
        assertInstanceOf(UserDetailsImpl.class, result);
    }

    @Test
    void loadUserByUsername_quandoUsuarioNaoExiste_deveLancarUsernameNotFoundException() {
        when(usuarioRepository.findByEmail("naoexiste@test.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> authService.loadUserByUsername("naoexiste@test.com"));
    }

    @Test
    void registerUser_comEmailNovo_deveCriarUsuario() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO("Maria", "maria@test.com", "senha123", TipoUsuario.PACIENTE, 1);
        Usuario entity = new Usuario("Maria", "maria@test.com", null, TipoUsuario.PACIENTE);
        Usuario saved = new Usuario("Maria", "maria@test.com", "encodedPassword", TipoUsuario.PACIENTE);

        when(usuarioRepository.existsByEmail("maria@test.com")).thenReturn(false);
        when(usuarioMapper.toEntity(dto)).thenReturn(entity);
        when(passwordEncoder.encode("senha123")).thenReturn("encodedPassword");
        when(usuarioRepository.save(entity)).thenReturn(saved);

        Usuario result = authService.registerUser(dto);

        assertNotNull(result);
        assertEquals("maria@test.com", result.getEmail());
        verify(passwordEncoder).encode("senha123");
        verify(usuarioRepository).save(entity);
    }

    @Test
    void registerUser_comEmailDuplicado_deveLancarRuntimeException() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO("Maria", "maria@test.com", "senha123", TipoUsuario.PACIENTE, 1);

        when(usuarioRepository.existsByEmail("maria@test.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> authService.registerUser(dto));
        verify(usuarioRepository, never()).save(any());
    }
}
