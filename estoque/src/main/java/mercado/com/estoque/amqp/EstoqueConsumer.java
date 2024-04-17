package mercado.com.estoque.amqp;


import mercado.com.estoque.Service.ItemService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

@Component
public class EstoqueConsumer {

    @Autowired
    private ItemService itemService;

    @RabbitListener(queues = "atualiza.estoque")
    public void receberMensagemAtualizacaoEstoque(MensagemAtualizacaoEstoque mensagem) {
        if (mensagem == null || mensagem.getIdItem() <= 0) {
            // Ignorar mensagens com ID inválido
            return;
        }
        long idItem = mensagem.getIdItem();
        int quantidadeVendida = mensagem.getQuantidade();

        itemService.atualizarEstoque(idItem, quantidadeVendida);
    }
}

