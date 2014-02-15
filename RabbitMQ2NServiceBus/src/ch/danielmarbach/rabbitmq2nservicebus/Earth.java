package ch.danielmarbach.rabbitmq2nservicebus;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Earth {

	public static void main(String[] args) {
		Connection connection = null;
		try {
			ExecutorService executor = Executors.newFixedThreadPool(10);

			ConnectionFactory factory = new ConnectionFactory();
			factory.setUri("amqp://brokers:5672");
			connection = factory.newConnection();

			System.out.println("RabbitMQ: Starting Warehouse raid...");

			WarehouseRaid warehouse = new WarehouseRaid(
					connection.createChannel());
			HashMap<String, HandleMessages> handlers = new HashMap<String, HandleMessages>();
			handlers.put(Constants.POLICE_OFFICER_DIED_EXCHANGE_NAME,
					new PoliceOfficerDiedHandler());
			handlers.put(Constants.QUEUE_NAME, new HuntDeadosOnEarthHandler());

			EarthConsumer consumer = new EarthConsumer(
					connection.createChannel(), handlers);
			consumer.start();
			executor.execute(warehouse);

			System.out.println("RabbitMQ: Press any key to abort raid...");
			System.in.read();

			warehouse.abort();
			consumer.stop();

			executor.shutdown();
			executor.awaitTermination(5000, TimeUnit.MILLISECONDS);

			System.out.println("RabbitMQ: Shutting down...");
		} catch (Exception e) {
			System.err.println("Caught: " + e);
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
