package ch.danielmarbach.rabbitmq2nservicebus;

import java.io.IOException;

public class Earth {

	public static void main(String[] args) {
		try {
			WarehouseRaid runnable = new WarehouseRaid();
			Thread raidThread = new Thread(runnable);
			raidThread.start();
			System.in.read();
		} catch (IOException e) {
			System.err.println("Caught: " + e);
			e.printStackTrace();
		}
	}
}
