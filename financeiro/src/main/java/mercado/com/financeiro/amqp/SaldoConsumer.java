package mercado.com.financeiro.amqp;



import mercado.com.financeiro.service.SaldoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SaldoConsumer {

    @Autowired
    private SaldoService saldoService;

    @RabbitListener(queues = "atualiza.saldo")
    public void receberMensagemAtualizacaoSaldo(@Payload MensagemAtualizacaoSaldo mensagem) {
        if (mensagem == null || mensagem.getValorvenda() == null) {
            return;
        }
        BigDecimal valorvenda = mensagem.getValorvenda();

        saldoService.adcionaSaldo( valorvenda);
    }
}

