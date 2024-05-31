package mercado.com.estoque.amqp;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class EstoqueAMQPConfiguration {
    @Bean
    public Jackson2JsonMessageConverter messageConverter(){
        return  new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);

        return  rabbitTemplate;
    }

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames("atualiza.estoque");
        container.setMessageListener(new EstoqueMessageListener());
        return container;
    }


    @Bean
    public Queue filaAtualizaEstoque() {
        return QueueBuilder
                .nonDurable("atualiza.estoque")
                .deadLetterExchange("atualizaestoque.dlx")
                .build();
    }


    @Bean
    public FanoutExchange fanoutExchange() {
        return ExchangeBuilder
                .fanoutExchange("transacoes.ex")
                .build();
    }

    @Bean
    public Binding bindEstoqueVenda(FanoutExchange fanoutExchange) {
        return BindingBuilder
                .bind(filaAtualizaEstoque())
                .to(fanoutExchange());
    }

    @Bean
    public FanoutExchange deadLetterExchange() {
        return new FanoutExchange("atualizaestoque.dlx");
    }

    @Bean
    public Queue atualizaestoqueDlq() {
        return QueueBuilder
                .nonDurable("atualizaestoque.dlq")
                .build();
    }

    @Bean
    public Binding bindDlxatualizaEstoque() {
        return BindingBuilder
                .bind(atualizaestoqueDlq())
                .to(deadLetterExchange());
    }

    @Bean
    public RabbitAdmin criaRabbitAdmin(ConnectionFactory conn) {
        return new RabbitAdmin(conn);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> inicializaAdmin(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }
}