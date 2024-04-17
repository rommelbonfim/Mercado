package mercado.com.financeiro.service;

import jakarta.persistence.EntityNotFoundException;

import mercado.com.financeiro.dto.SaldoDto;
import mercado.com.financeiro.models.Saldo;
import mercado.com.financeiro.repository.SaldoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
public class SaldoService {
    @Autowired
    private SaldoRepository repository;



    @Autowired
    private ModelMapper modelMapper;

    public Page<SaldoDto> obterTodos(Pageable paginacao) {
        return repository
                .findAll(paginacao)
                .map(p -> modelMapper.map(p, SaldoDto.class));
    }

    public SaldoDto adcionarVenda(SaldoDto dto) {

        Saldo saldo = modelMapper.map(dto, Saldo.class);
        repository.save(saldo);

        return modelMapper.map(saldo, SaldoDto.class);
    }

    public SaldoDto atualizarSaldo(Long id, SaldoDto dto) {
        Saldo saldo = modelMapper.map(dto, Saldo.class);
        saldo.setId(id);
        saldo = repository.save(saldo);
        return modelMapper.map(saldo, SaldoDto.class);
    }

    @Transactional
    public void adcionaSaldo(BigDecimal valorVenda) {
        Saldo saldo = repository.findById(1L).orElse(null);

        if (saldo == null) {
            saldo = new Saldo();
            saldo.setSaldo(BigDecimal.ZERO);
        }

        BigDecimal saldoAtual = saldo.getSaldo();
        if (saldoAtual == null) {
            throw new IllegalStateException("O saldo atual não pode ser nulo");
        }

        if (valorVenda == null) {
            throw new IllegalArgumentException("O valor da venda não pode ser nulo");
        }
        BigDecimal novoSaldo = saldoAtual.add(valorVenda);

        saldo.setSaldo(novoSaldo);
        repository.save(saldo);
    }


}
