package ch.danielmarbach.rabbitmq2nservicebus;

import java.io.IOException;
import java.util.HashMap;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class EarthConsumer {

	private final Channel _channel;
	private final HashMap<String, HandleMessages> _handlers;

	public EarthConsumer(Channel channel,
			HashMap<String, HandleMessages> handlers) {
		_channel = channel;
		_handlers = handlers;
	}

	public void start() throws IOException {
		_channel.queueDeclare(Constants.QUEUE_NAME, true, false, false, null);
		_channel.basicQos(1);
		_channel.basicConsume(Constants.QUEUE_NAME, true, "EarthConsumer",
				new DefaultConsumer(_channel) {
					@Override
					public void handleDelivery(String consumerTag,
							Envelope envelope, AMQP.BasicProperties properties,
							byte[] body) throws IOException {

						String exchange = envelope.getExchange();
						if (_handlers.containsKey(exchange)) {
							_handlers.get(exchange).handle(_channel,
									properties, body);
						}
					}
				});
	}

	public void stop() throws IOException {
		_channel.basicCancel("EarthConsumer");
	}

}
