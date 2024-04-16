package mercado.com.financeiro.service;

import mercado.com.financeiro.dto.SaldoDto;
import mercado.com.financeiro.models.Saldo;
import mercado.com.financeiro.repository.SaldoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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


}
