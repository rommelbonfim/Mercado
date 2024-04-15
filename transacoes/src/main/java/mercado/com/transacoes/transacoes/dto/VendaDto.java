package mercado.com.transacoes.transacoes.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class VendaDto {
    private long id;
    private long iditem;
    private Integer quantidade;
    private BigDecimal valorvenda;
}
