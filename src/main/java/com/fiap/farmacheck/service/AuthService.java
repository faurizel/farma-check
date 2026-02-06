package com.fiap.farmacheck.service;

import com.fiap.farmacheck.mapper.UsuarioMapper;
import com.fiap.farmacheck.model.dto.usuario.UsuarioRequestDTO;
import com.fiap.farmacheck.model.entity.Usuario;
import com.fiap.farmacheck.repository.UsuarioRepository;
import com.fiap.farmacheck.security.service.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + email));
        return new UserDetailsImpl(usuario);
    }

    public Usuario registerUser(UsuarioRequestDTO dto) {
        if (this.usuarioRepository.existsByEmail(dto.email())) {
            throw new RuntimeException("Email já cadastrado.");
        }

        Usuario newUser = usuarioMapper.toEntity(dto);

        String encryptedPassword = passwordEncoder.encode(dto.senha());
        newUser.setSenha(encryptedPassword);

        return this.usuarioRepository.save(newUser);
    }
}