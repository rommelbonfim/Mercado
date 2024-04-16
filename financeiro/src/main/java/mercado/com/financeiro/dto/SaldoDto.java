package mercado.com.financeiro.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class SaldoDto {
    private long id;
    private BigDecimal saldo;
}
