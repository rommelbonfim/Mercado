package mercado.com.estoque.amqp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MensagemAtualizacaoEstoque {
    private long idItem;
    private int quantidade;



    public MensagemAtualizacaoEstoque(long idItem, int quantidade) {
        this.idItem = idItem;
        this.quantidade = quantidade;
    }


}
