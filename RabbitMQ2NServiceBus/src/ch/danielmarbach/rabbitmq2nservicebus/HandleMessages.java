package ch.danielmarbach.rabbitmq2nservicebus;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

public interface HandleMessages {
	void handle(Channel channel, AMQP.BasicProperties properties, byte[] body);
}
