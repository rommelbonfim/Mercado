package mercado.com.financeiro.amqp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class MensagemAtualizacaoSaldo {

    private BigDecimal valorvenda;



    public MensagemAtualizacaoSaldo(BigDecimal valorvenda) {

        this.valorvenda = valorvenda;
    }


}
