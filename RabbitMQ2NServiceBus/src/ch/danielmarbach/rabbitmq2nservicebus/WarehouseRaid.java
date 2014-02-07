package ch.danielmarbach.rabbitmq2nservicebus;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class WarehouseRaid implements Runnable {

	@Override
	public void run() {
		try {
			ConnectionFactory cfconn = new ConnectionFactory();
			cfconn.setUri("amqp://localhost");
			Connection conn = cfconn.newConnection();

			Channel ch = conn.createChannel();
		} catch (Exception e) {
			System.err.println("WarehouseRaid thread caught exception: " + e);
			e.printStackTrace();
			System.exit(1);
		}
	}

}
