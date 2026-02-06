package com.fiap.farmacheck.controller;

import com.fiap.farmacheck.model.dto.token.TokenResponseDTO;
import com.fiap.farmacheck.model.dto.usuario.UsuarioLoginRequestDTO;
import com.fiap.farmacheck.model.dto.usuario.UsuarioRequestDTO;
import com.fiap.farmacheck.security.service.UserDetailsImpl;
import com.fiap.farmacheck.service.AuthService;
import com.fiap.farmacheck.service.JwtTokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final AuthService authService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.authService = authService;
    }

    @PostMapping("/auth/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponseDTO authenticateUser(@Valid @RequestBody UsuarioLoginRequestDTO dto) {

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.senha());

        Authentication authentication = authenticationManager.authenticate(authToken);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String token = jwtTokenService.generateToken(userDetails);

        return new TokenResponseDTO(token);
    }

    @PostMapping("/auth/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@Valid @RequestBody UsuarioRequestDTO dto) {
        authService.registerUser(dto);
    }

    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> getAuthenticationTest() {
        return new ResponseEntity<>("Autenticado com sucesso", HttpStatus.OK);
    }

    @GetMapping("/test/administrador")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> getAdministradorAuthenticationTest() {
        return new ResponseEntity<>("Administrador(a) autenticado com sucesso", HttpStatus.OK);
    }

    @GetMapping("/test/paciente")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> getPacienteAuthenticationTest() {
        return new ResponseEntity<>("Paciente autenticado com sucesso", HttpStatus.OK);
    }
}