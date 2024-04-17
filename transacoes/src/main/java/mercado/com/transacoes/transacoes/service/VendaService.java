package mercado.com.transacoes.transacoes.service;

import mercado.com.transacoes.transacoes.amqp.MensagemAtualizacaoEstoque;
import mercado.com.transacoes.transacoes.amqp.MensagemRegistroTransacao;
import mercado.com.transacoes.transacoes.dto.VendaDto;
import mercado.com.transacoes.transacoes.models.Venda;
import mercado.com.transacoes.transacoes.repository.VendaRepository;
import org.modelmapper.ModelMapper;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class VendaService {
    @Autowired
    private VendaRepository repository;

    @Autowired
    private EstoqueServiceClient estoqueClient;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private FanoutExchange fanoutExchange;

    public Page<VendaDto> obterTodos(Pageable paginacao) {
        return repository
                .findAll(paginacao)
                .map(p -> modelMapper.map(p, VendaDto.class));
    }

    public VendaDto adcionarVenda(VendaDto dto) {
        BigDecimal precoProduto = estoqueClient.obterPrecoProdutoPorId(dto.getIditem());

        BigDecimal valorVenda = precoProduto.multiply(BigDecimal.valueOf(dto.getQuantidade()));

        dto.setValorvenda(valorVenda);

        rabbitTemplate.convertAndSend(fanoutExchange.getName(), "", new MensagemAtualizacaoEstoque(dto.getIditem(), dto.getQuantidade()));
        rabbitTemplate.convertAndSend(fanoutExchange.getName(), "", new MensagemRegistroTransacao( dto.getValorvenda()));


        Venda venda = modelMapper.map(dto, Venda.class);
        Venda vendaSalva = repository.save(venda);

        return modelMapper.map(vendaSalva, VendaDto.class);
    }


}
