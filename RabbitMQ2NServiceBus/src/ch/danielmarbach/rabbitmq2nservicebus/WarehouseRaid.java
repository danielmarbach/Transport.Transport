package ch.danielmarbach.rabbitmq2nservicebus;

import java.io.IOException;
import java.util.UUID;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class WarehouseRaid implements Runnable {
	private volatile boolean run = true;
	private final static String QUEUE_NAME = "Earth.Native";
	private final static String EXCHANGE_NAME = "Messages.Events:PoliceOfficerDied";

	@Override
	public void run() {
		Connection connection = null;
		Channel channel = null;
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setUri("amqp://brokers:5672");

			connection = factory.newConnection();
			channel = connection.createChannel();

			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			channel.exchangeDeclare(EXCHANGE_NAME, "fanout", true);
			channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");

			int numberOfOfficers = 1;

			while (run) {
				BasicProperties props = new AMQP.BasicProperties().builder()
						.messageId(UUID.randomUUID().toString()).build();

				String message = String
						.format("<?xml version=\"1.0\" ?>"
								+ "<PoliceOfficerDied xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns=\"http://tempuri.net/Messages.Events\">"
								+ "<Identification>%s</Identification>"
								+ "<Name>Nick Walker the %sth</Name>"
								+ "</PoliceOfficerDied>", UUID.randomUUID()
								.toString(), numberOfOfficers++);

				channel.basicPublish(EXCHANGE_NAME, "", props,
						message.getBytes());

				Thread.sleep(5000);
			}

		} catch (Exception e) {
			System.err.println("WarehouseRaid thread caught exception: " + e);
			e.printStackTrace();
			System.exit(1);
		} finally {
			try {
				channel.close();
				connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void abort() {
		run = false;
	}

}
