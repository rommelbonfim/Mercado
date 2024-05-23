package mercado.com.estoque.amqp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomMessageRecoverer implements MessageRecoverer {

    @Autowired
    private EstoqueAMQPConfiguration amqpConfiguration;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void recover(Message message, Throwable cause) {
        try {
            // Obtenha o conteúdo original da mensagem
            byte[] originalBody = message.getBody();
            String originalMessage = new String(originalBody, StandardCharsets.UTF_8);

            // Obtenha a mensagem de erro
            String errorMessage = cause.getMessage();

            // Crie um mapa com o conteúdo original e a mensagem de erro
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("originalMessage", originalMessage);
            messageData.put("errorMessage", errorMessage);

            // Converta o mapa para JSON
            byte[] newBody = objectMapper.writeValueAsBytes(messageData);

            // Crie uma nova mensagem com o conteúdo atualizado
            MessageProperties props = new MessageProperties();
            Message newMessage = MessageBuilder.withBody(newBody)
                    .andProperties(props)
                    .build();

            // Envie a nova mensagem para a dead letter queue
            rabbitTemplate.send("atualizaestoque.dlq", newMessage);
        } catch (Exception e) {
            // Lide com qualquer exceção que possa ocorrer
            e.printStackTrace();
        }
    }
}
