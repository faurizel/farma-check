package com.fiap.farmacheck.controller;

import com.fiap.farmacheck.model.dto.unidade.UnidadeRequestDTO;
import com.fiap.farmacheck.model.dto.unidade.UnidadeResponseDTO;
import com.fiap.farmacheck.service.UnidadeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/unidades")
public class UnidadeController {

    private final UnidadeService unidadeService;

    public UnidadeController(UnidadeService unidadeService) {
        this.unidadeService = unidadeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UnidadeResponseDTO criar(@Valid @RequestBody UnidadeRequestDTO dto) {
        return unidadeService.criar(dto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UnidadeResponseDTO> listarTodos() {
        return unidadeService.listarTodos();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UnidadeResponseDTO buscarPorId(@PathVariable Integer id) {
        return unidadeService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UnidadeResponseDTO atualizar(@PathVariable Integer id,
                                         @Valid @RequestBody UnidadeRequestDTO dto) {
        return unidadeService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        unidadeService.deletar(id);
    }
}
