package mercado.com.financeiro.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import mercado.com.financeiro.dto.SaldoDto;
import mercado.com.financeiro.models.Saldo;
import mercado.com.financeiro.service.SaldoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/saldo")
public class SaldoController {
    @Autowired
    private SaldoService service;

    @GetMapping
    public Page<SaldoDto> listar(@PageableDefault(size = 1) Pageable paginacao) {
        return service.obterTodos(paginacao);
    }

    @PostMapping
    public ResponseEntity<SaldoDto> cadastrar(@RequestBody @Valid SaldoDto dto, UriComponentsBuilder uriBuilder) {
        SaldoDto saldo = service.adcionarVenda(dto);
        URI endereco = uriBuilder.path("/saldo/{id}").buildAndExpand(saldo.getId()).toUri();



        return ResponseEntity.created(endereco).body(saldo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaldoDto> atualizar(@PathVariable @NotNull Long id, @RequestBody @Valid SaldoDto dto) {
        SaldoDto atualizado = service.atualizarSaldo(id, dto);
        return ResponseEntity.ok(atualizado);
    }

}
