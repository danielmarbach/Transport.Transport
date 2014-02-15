package ch.danielmarbach.rabbitmq2nservicebus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;

public class PoliceOfficerDiedHandler implements HandleMessages {

	@Override
	public void handle(Channel channel, BasicProperties properties, byte[] body) {
		try {
			Extractor extractor = new Extractor(body);

			String officerName = extractor.extract("/PoliceOfficerDied/Name");
			String officerId = extractor
					.extract("/PoliceOfficerDied/Identification");

			System.out.println(String.format(
					"Police Officer %s will ascend to heaven.", officerName));

			Map<String, Object> headers = new HashMap<String, Object>();

			BasicProperties props = new AMQP.BasicProperties().builder()
					.messageId(UUID.randomUUID().toString())
					.contentType("text/xml").deliveryMode(2).headers(headers)
					.build();

			String message = String
					.format("<?xml version=\"1.0\" ?>"
							+ "<AscendToHeaven xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns=\"http://tempuri.net/Messages.Commands\">"
							+ "<PoliceOfficer>%s</PoliceOfficer>"
							+ "</AscendToHeaven>", officerId);

			channel.basicPublish("", Constants.HEAVEN_QUEUE_NAME, props,
					message.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
