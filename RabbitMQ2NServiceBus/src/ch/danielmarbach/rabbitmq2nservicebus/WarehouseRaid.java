package ch.danielmarbach.rabbitmq2nservicebus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;

public class WarehouseRaid implements Runnable {
	private final Channel channel;
	private volatile boolean run = true;

	public WarehouseRaid(Channel channel) {
		this.channel = channel;
	}

	@Override
	public void run() {
		try {
			channel.queueDeclare(Constants.QUEUE_NAME, true, false, false, null);
			channel.exchangeDeclare(Constants.EARTH_EXCHANGE_NAME, "fanout",
					true);
			channel.exchangeDeclare(
					Constants.POLICE_OFFICER_DIED_EXCHANGE_NAME, "fanout", true);
			channel.queueBind(Constants.QUEUE_NAME,
					Constants.EARTH_EXCHANGE_NAME, "");
			channel.queueBind(Constants.QUEUE_NAME,
					Constants.POLICE_OFFICER_DIED_EXCHANGE_NAME, "");

			int numberOfOfficers = 1;

			while (run) {

				Map<String, Object> headers = new HashMap<String, Object>();

				BasicProperties props = new AMQP.BasicProperties().builder()
						.messageId(UUID.randomUUID().toString())
						.contentType("text/xml").deliveryMode(2)
						.headers(headers).build();

				String message = String
						.format("<?xml version=\"1.0\" ?>"
								+ "<PoliceOfficerDied xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns=\"http://tempuri.net/Messages.Events\">"
								+ "<Identification>%s</Identification>"
								+ "<Name>Nick Walker (Native) the %sth</Name>"
								+ "</PoliceOfficerDied>", UUID.randomUUID()
								.toString(), numberOfOfficers++);

				channel.basicPublish(
						Constants.POLICE_OFFICER_DIED_EXCHANGE_NAME, "", props,
						message.getBytes());

				Thread.sleep(5000);
			}

		} catch (Exception e) {
			System.err.println("WarehouseRaid thread caught exception: " + e);
			e.printStackTrace();
		} finally {
			try {
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void abort() {
		run = false;
	}

}
