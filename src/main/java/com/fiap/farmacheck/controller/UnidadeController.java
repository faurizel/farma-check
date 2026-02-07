package com.fiap.farmacheck.controller;

import com.fiap.farmacheck.model.dto.unidade.UnidadeRequestDTO;
import com.fiap.farmacheck.model.dto.unidade.UnidadeResponseDTO;
import com.fiap.farmacheck.service.UnidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/unidades")
@Tag(name = "Unidades")
public class UnidadeController {

    private final UnidadeService unidadeService;

    public UnidadeController(UnidadeService unidadeService) {
        this.unidadeService = unidadeService;
    }

    @Operation(summary = "Cadastra uma nova unidade")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UnidadeResponseDTO criar(@Valid @RequestBody UnidadeRequestDTO dto) {
        return unidadeService.criar(dto);
    }

    @Operation(summary = "Lista todas as unidades")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UnidadeResponseDTO> listarTodos() {
        return unidadeService.listarTodos();
    }

    @Operation(summary = "Busca unidade por ID")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UnidadeResponseDTO buscarPorId(@PathVariable Integer id) {
        return unidadeService.buscarPorId(id);
    }

    @Operation(summary = "Atualiza uma unidade")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UnidadeResponseDTO atualizar(@PathVariable Integer id,
                                         @Valid @RequestBody UnidadeRequestDTO dto) {
        return unidadeService.atualizar(id, dto);
    }

    @Operation(summary = "Deleta uma unidade")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        unidadeService.deletar(id);
    }
}
