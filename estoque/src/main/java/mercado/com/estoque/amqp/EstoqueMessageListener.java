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
import java.nio.charset.StandardCharsets;
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
        } catch (Exception e) {
            System.out.println("Exceção capturada: " + e.getMessage());

            String errorMessage = e.getMessage();
            byte[] originalBody = message.getBody();
            String originalMessage = new String(originalBody, StandardCharsets.UTF_8);

            System.out.println("Mensagem original: " + originalMessage);

            // Obter a mensagem de erro detalhada
            String detailedErrorMessage;
            if (e.getCause() != null && e.getCause().getMessage() != null) {
                detailedErrorMessage = "Erro ao processar a mensagem: " + e.getCause().getMessage();
            } else {
                detailedErrorMessage = "Erro ao processar a mensagem: " + errorMessage;
            }

            System.out.println("Mensagem de erro detalhada: " + detailedErrorMessage);

            // Criar um mapa com o corpo original e a mensagem de erro detalhada
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("originalMessage", originalMessage);
            messageData.put("detailedErrorMessage", detailedErrorMessage);

            System.out.println("Mapa de mensagem: " + messageData);

            // Converter o mapa para JSON
            byte[] newBody;
            try {
                newBody = objectMapper.writeValueAsBytes(messageData);
            } catch (JsonProcessingException ex) {
                // Lidar com erros de serialização JSON
                ex.printStackTrace();
                return;
            }

            System.out.println("Novo corpo da mensagem: " + new String(newBody, StandardCharsets.UTF_8));

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

