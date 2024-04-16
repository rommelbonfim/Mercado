package mercado.com.estoque.Service;

import jakarta.persistence.EntityNotFoundException;
import mercado.com.estoque.DTO.ItemDto;
import mercado.com.estoque.models.Item;
import mercado.com.estoque.repository.ItemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ItemService {

    @Autowired
    private ItemRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    public Page<ItemDto> obterTodos(Pageable paginacao) {
        return repository
                .findAll(paginacao)
                .map(p -> modelMapper.map(p, ItemDto.class));
    }

    public ItemDto obterPorId(Long id) {
        Item item = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());

        return modelMapper.map(item, ItemDto.class);
    }

    public ItemDto obterPorNome(String nome) {
        Item item = repository.findByNome(nome)
                .orElseThrow(() -> new EntityNotFoundException());

        return modelMapper.map(item, ItemDto.class);
    }

    public ItemDto adcionarItem(ItemDto dto) {
        Item item = modelMapper.map(dto, Item.class);
        repository.save(item);
        return modelMapper.map(item, ItemDto.class);
    }

    public ItemDto atualizarItem(Long id, ItemDto dto) {
        Item item = modelMapper.map(dto, Item.class);
        item.setId(id);
        item = repository.save(item);
        return modelMapper.map(item, ItemDto.class);
    }

    public void excluirItem(Long id) {
        repository.deleteById(id);
    }

    public BigDecimal obterPrecoProdutoPorId(Long id) {
        Item itemDto = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com o ID: " + id));

        return itemDto.getValor();
    }
}
