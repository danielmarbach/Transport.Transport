package ch.danielmarbach.activemq2nservicebus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Earth {

	public static void main(String args[]) {
		Connection connection = null;
		try {
			ExecutorService executor = Executors.newFixedThreadPool(10);

			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
					"failover:(tcp://brokers:61616,tcp://brokers:61616)?randomize=false");

			connection = connectionFactory.createConnection();
			connection.start();

			System.out.println("ActiveMQ: Starting Warehouse raid...");

			// Create a Session
			EarthConsumer consumer = new EarthConsumer(
					connection.createSession(false, Session.AUTO_ACKNOWLEDGE));
			consumer.start();
			WarehouseRaid warehouse = new WarehouseRaid(
					connection.createSession(false, Session.AUTO_ACKNOWLEDGE));
			executor.execute(warehouse);

			System.out.println("ActiveMQ: Press any key to abort raid...");
			System.in.read();

			warehouse.abort();

			executor.shutdown();
			executor.awaitTermination(5000, TimeUnit.MILLISECONDS);

			System.out.println("ActiveMQ: Shutting down...");

		} catch (Exception e) {
			System.err.println("Caught: " + e);
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}