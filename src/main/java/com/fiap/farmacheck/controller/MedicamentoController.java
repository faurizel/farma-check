package com.fiap.farmacheck.controller;

import com.fiap.farmacheck.model.dto.disponibilidade.DisponibilidadeRequestDTO;
import com.fiap.farmacheck.model.dto.disponibilidade.DisponibilidadeResponseDTO;
import com.fiap.farmacheck.model.dto.medicamento.MedicamentoRequestDTO;
import com.fiap.farmacheck.model.dto.medicamento.MedicamentoResponseDTO;
import com.fiap.farmacheck.model.entity.Usuario;
import com.fiap.farmacheck.repository.UsuarioRepository;
import com.fiap.farmacheck.service.MedicamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicamentos")
@Tag(name = "Medicamentos")
public class MedicamentoController {

    private final MedicamentoService medicamentoService;
    private final UsuarioRepository usuarioRepository;

    public MedicamentoController(MedicamentoService medicamentoService,
                                  UsuarioRepository usuarioRepository) {
        this.medicamentoService = medicamentoService;
        this.usuarioRepository = usuarioRepository;
    }

    @Operation(summary = "Cadastra um novo medicamento")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MedicamentoResponseDTO criar(@Valid @RequestBody MedicamentoRequestDTO dto) {
        return medicamentoService.criar(dto);
    }

    @Operation(summary = "Lista todos os medicamentos")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MedicamentoResponseDTO> listarTodos() {
        return medicamentoService.listarTodos();
    }

    @Operation(summary = "Busca medicamento por ID")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MedicamentoResponseDTO buscarPorId(@PathVariable Integer id) {
        return medicamentoService.buscarPorId(id);
    }

    @Operation(summary = "Atualiza um medicamento")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MedicamentoResponseDTO atualizar(@PathVariable Integer id,
                                             @Valid @RequestBody MedicamentoRequestDTO dto) {
        return medicamentoService.atualizar(id, dto);
    }

    @Operation(summary = "Deleta um medicamento")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        medicamentoService.deletar(id);
    }

    @Operation(summary = "Verifica disponibilidade de um medicamento")
    @PostMapping("/disponibilidade")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DisponibilidadeResponseDTO> verificarDisponibilidade(
            @Valid @RequestBody DisponibilidadeRequestDTO dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        DisponibilidadeResponseDTO response = medicamentoService.verificarDisponibilidade(
                dto.nomeMedicamento(),
                usuario.getEmail(),
                usuario.getNome()
        );

        return ResponseEntity.ok(response);
    }
}
