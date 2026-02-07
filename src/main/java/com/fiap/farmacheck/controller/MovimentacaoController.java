package com.fiap.farmacheck.controller;

import com.fiap.farmacheck.model.dto.movimentacao.MovimentacaoRequestDTO;
import com.fiap.farmacheck.model.dto.movimentacao.MovimentacaoResponseDTO;
import com.fiap.farmacheck.service.MovimentacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimentacoes")
public class MovimentacaoController {

    private final MovimentacaoService movimentacaoService;

    public MovimentacaoController(MovimentacaoService movimentacaoService) {
        this.movimentacaoService = movimentacaoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovimentacaoResponseDTO criar(@Valid @RequestBody MovimentacaoRequestDTO dto) {
        return movimentacaoService.criar(dto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MovimentacaoResponseDTO> listarTodos() {
        return movimentacaoService.listarTodos();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MovimentacaoResponseDTO buscarPorId(@PathVariable Integer id) {
        return movimentacaoService.buscarPorId(id);
    }
}
