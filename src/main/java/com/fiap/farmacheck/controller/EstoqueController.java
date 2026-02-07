package com.fiap.farmacheck.controller;

import com.fiap.farmacheck.model.dto.estoque.EstoqueRequestDTO;
import com.fiap.farmacheck.model.dto.estoque.EstoqueResponseDTO;
import com.fiap.farmacheck.service.EstoqueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estoques")
@Tag(name = "Estoques")
public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @Operation(summary = "Cadastra um novo registro de estoque")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EstoqueResponseDTO criar(@Valid @RequestBody EstoqueRequestDTO dto) {
        return estoqueService.criar(dto);
    }

    @Operation(summary = "Lista todos os registros de estoque")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EstoqueResponseDTO> listarTodos() {
        return estoqueService.listarTodos();
    }

    @Operation(summary = "Busca estoque por ID")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EstoqueResponseDTO buscarPorId(@PathVariable Integer id) {
        return estoqueService.buscarPorId(id);
    }

    @Operation(summary = "Atualiza um registro de estoque")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EstoqueResponseDTO atualizar(@PathVariable Integer id,
                                         @Valid @RequestBody EstoqueRequestDTO dto) {
        return estoqueService.atualizar(id, dto);
    }

    @Operation(summary = "Deleta um registro de estoque")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        estoqueService.deletar(id);
    }
}
