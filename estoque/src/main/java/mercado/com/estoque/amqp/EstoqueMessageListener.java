package mercado.com.estoque.amqp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mercado.com.estoque.amqp.EstoqueConsumer;
import mercado.com.estoque.amqp.MensagemAtualizacaoEstoque;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class EstoqueMessageListener implements MessageListener {

    @Autowired
    private EstoqueConsumer estoqueConsumer;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void onMessage(Message message) {
        try {
            // Deserializar o JSON da mensagem
            Map<String, Object> mensagemData = objectMapper.readValue(message.getBody(), Map.class);

            // Extrair os dados necessários
            long idItem = Long.parseLong(mensagemData.get("idItem").toString());
            int quantidade = Integer.parseInt(mensagemData.get("quantidade").toString());

            // Criar objeto MensagemAtualizacaoEstoque
            MensagemAtualizacaoEstoque mensagem = new MensagemAtualizacaoEstoque(idItem, quantidade);

            // Chamar o método do EstoqueConsumer
            estoqueConsumer.receberMensagemAtualizacaoEstoque(mensagem);
        } catch (IOException e) {
            String errorMessage = e.getMessage();
            byte[] originalBody = message.getBody();

            // Criar um mapa com o corpo original e o erro
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("originalMessage", new String(originalBody));
            messageData.put("errorMessage", errorMessage);

            // Converter o mapa para JSON
            byte[] newBody;
            try {
                newBody = objectMapper.writeValueAsBytes(messageData);
            } catch (JsonProcessingException ex) {
                // Lidar com erros de serialização JSON
                ex.printStackTrace();
                return;
            }

            // Criar uma nova mensagem com o corpo atualizado
            MessageProperties props = new MessageProperties();
            Message newMessage = MessageBuilder.withBody(newBody)
                    .andProperties(props)
                    .build();

            // Enviar a nova mensagem para a dead letter queue
            rabbitTemplate.send("atualizaestoque.dlq", newMessage);
        }
    }
}
