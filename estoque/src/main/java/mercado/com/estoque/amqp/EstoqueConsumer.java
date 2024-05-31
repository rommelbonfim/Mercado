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

        try {
            itemService.atualizarEstoque(idItem, quantidadeVendida);
        } catch (Exception e) {
            // Capturar a exceção específica lançada pelo itemService
            String errorMessage = "Erro ao atualizar o estoque do item " + idItem + " com a quantidade " + quantidadeVendida + ". " + e.getMessage();
            throw new RuntimeException(errorMessage, e);
        }
    }
}

