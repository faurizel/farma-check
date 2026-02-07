package com.fiap.farmacheck.controller;

import com.fiap.farmacheck.model.dto.disponibilidade.DisponibilidadeRequestDTO;
import com.fiap.farmacheck.model.dto.disponibilidade.DisponibilidadeResponseDTO;
import com.fiap.farmacheck.model.entity.Usuario;
import com.fiap.farmacheck.repository.UsuarioRepository;
import com.fiap.farmacheck.service.DisponibilidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/disponibilidade")
@Tag(name = "Disponibilidade")
public class DisponibilidadeController {

    private final DisponibilidadeService disponibilidadeService;
    private final UsuarioRepository usuarioRepository;

    public DisponibilidadeController(DisponibilidadeService disponibilidadeService,
                                     UsuarioRepository usuarioRepository) {
        this.disponibilidadeService = disponibilidadeService;
        this.usuarioRepository = usuarioRepository;
    }

    @Operation(summary = "Verifica disponibilidade de um medicamento nas farmacias publicas")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DisponibilidadeResponseDTO> verificarDisponibilidade(
            @Valid @RequestBody DisponibilidadeRequestDTO dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));

        DisponibilidadeResponseDTO response = disponibilidadeService.verificarDisponibilidade(
                dto.nomeMedicamento(),
                usuario.getEmail(),
                usuario.getNome()
        );

        return ResponseEntity.ok(response);
    }
}
