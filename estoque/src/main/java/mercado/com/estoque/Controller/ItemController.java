package mercado.com.estoque.Controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import mercado.com.estoque.DTO.ItemDto;
import mercado.com.estoque.Service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;

@RestController
@RequestMapping("/Estoque")
public class ItemController {

    @Autowired
    private ItemService service;

    @GetMapping
    public Page<ItemDto> listar(@PageableDefault(size = 10) Pageable paginacao) {
        return service.obterTodos(paginacao);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> detalharId(@PathVariable @NotNull Long id) {
        ItemDto dto = service.obterPorId(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{nome}")
    public ResponseEntity<ItemDto> detalharNome(@PathVariable @NotBlank String nome) {
        ItemDto dto = service.obterPorNome(nome);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ItemDto> cadastrar(@RequestBody @Valid ItemDto dto, UriComponentsBuilder uriBuilder) {
        ItemDto item = service.adcionarItem(dto);
        URI endereco = uriBuilder.path("/estoque/{id}").buildAndExpand(item.getId()).toUri();



        return ResponseEntity.created(endereco).body(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemDto> atualizar(@PathVariable @NotNull Long id, @RequestBody @Valid ItemDto dto) {
        ItemDto atualizado = service.atualizarItem(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @GetMapping("/{id}/valor")
    public ResponseEntity<BigDecimal> obterPrecoProdutoPorId(@PathVariable Long id) {
        BigDecimal valor = service.obterPrecoProdutoPorId(id);
        return ResponseEntity.ok(valor);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ItemDto> remover(@PathVariable @NotNull Long id) {
        service.excluirItem(id);
        return ResponseEntity.noContent().build();
    }
}
