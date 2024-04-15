package mercado.com.transacoes.transacoes.controller;


import jakarta.validation.Valid;
import mercado.com.transacoes.transacoes.dto.VendaDto;
import mercado.com.transacoes.transacoes.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/vendas")
public class VendaController {
    @Autowired
    private VendaService service;

    @GetMapping
    public Page<VendaDto> listar(@PageableDefault(size = 10) Pageable paginacao) {
        return service.obterTodos(paginacao);
    }

    @PostMapping
    public ResponseEntity<VendaDto> cadastrar(@RequestBody @Valid VendaDto dto, UriComponentsBuilder uriBuilder) {
        VendaDto venda = service.adcionarVenda(dto);
        URI endereco = uriBuilder.path("/vendas/{id}").buildAndExpand(venda.getId()).toUri();



        return ResponseEntity.created(endereco).body(venda);
    }

}
