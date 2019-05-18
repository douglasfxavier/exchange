import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogs {

    private static final String EXCHANGE_NAME =  "logs";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        String queuName = channel.queueDeclare().getQueue();
        channel.queueBind(queuName, EXCHANGE_NAME,"");

        System.out.println(" [*] Esperando pelas mensagens. Para sair pression CTRL + C");

        DeliverCallback deliverCallback= (consumerTag, delivery) -> {
          String message = new String(delivery.getBody(), "UTF-8");
          System.out.println(" [X] Recebida '" + message + "'");
        };

        channel.basicConsume(queuName,true,deliverCallback, consumerTag -> {});
    }
}
