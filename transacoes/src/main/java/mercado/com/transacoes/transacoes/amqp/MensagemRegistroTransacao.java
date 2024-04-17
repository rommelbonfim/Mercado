package mercado.com.transacoes.transacoes.amqp;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class MensagemRegistroTransacao {

    private BigDecimal valorvenda;



    public MensagemRegistroTransacao( BigDecimal valorvenda) {

        this.valorvenda = valorvenda;
    }


}
