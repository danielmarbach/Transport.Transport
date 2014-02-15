package ch.danielmarbach.rabbitmq2nservicebus;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;

public class HuntDeadosOnEarthHandler implements HandleMessages {

	@Override
	public void handle(Channel channel, BasicProperties properties, byte[] body) {
		Extractor extractor = new Extractor(body);

		String officerName = extractor.extract("/HuntDeadosOnEarth/Name");

		System.out.println(String.format(
				"Police Officer %s is back hunting deados!", officerName));

	}
}
