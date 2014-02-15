package ch.danielmarbach.rabbitmq2nservicebus;

import java.io.IOException;
import java.util.HashMap;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class EarthConsumer {

	private final Channel channel;
	private final HashMap<String, HandleMessages> handlers;

	public EarthConsumer(Channel channel,
			HashMap<String, HandleMessages> handlers) {
		this.channel = channel;
		this.handlers = handlers;
	}

	public void start() throws IOException {
		channel.queueDeclare(Constants.QUEUE_NAME, true, false, false, null);
		channel.basicQos(1);
		channel.basicConsume(Constants.QUEUE_NAME, true, "EarthConsumer",
				new DefaultConsumer(channel) {
					@Override
					public void handleDelivery(String consumerTag,
							Envelope envelope, AMQP.BasicProperties properties,
							byte[] body) throws IOException {

						String exchange = envelope.getExchange();
						if (handlers.containsKey(exchange)) {
							handlers.get(exchange).handle(channel, properties,
									body);
						}
					}
				});
	}

	public void stop() throws IOException {
		channel.basicCancel("EarthConsumer");
	}

}
