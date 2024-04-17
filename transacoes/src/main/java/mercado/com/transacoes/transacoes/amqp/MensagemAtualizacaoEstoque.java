package mercado.com.transacoes.transacoes.amqp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MensagemAtualizacaoEstoque {
    private long idItem;
    private int quantidade;

    public MensagemAtualizacaoEstoque(long iditem, int quantidade) {
        this.idItem = iditem;
        this.quantidade = quantidade;
    }


}
