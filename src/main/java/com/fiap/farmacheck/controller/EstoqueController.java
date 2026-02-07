package com.fiap.farmacheck.controller;

import com.fiap.farmacheck.model.dto.estoque.EstoqueRequestDTO;
import com.fiap.farmacheck.model.dto.estoque.EstoqueResponseDTO;
import com.fiap.farmacheck.service.EstoqueService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estoques")
public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EstoqueResponseDTO criar(@Valid @RequestBody EstoqueRequestDTO dto) {
        return estoqueService.criar(dto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EstoqueResponseDTO> listarTodos() {
        return estoqueService.listarTodos();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EstoqueResponseDTO buscarPorId(@PathVariable Integer id) {
        return estoqueService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EstoqueResponseDTO atualizar(@PathVariable Integer id,
                                         @Valid @RequestBody EstoqueRequestDTO dto) {
        return estoqueService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        estoqueService.deletar(id);
    }
}
