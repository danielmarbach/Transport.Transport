package ch.danielmarbach.activemq2nservicebus;

import java.util.UUID;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

public class WarehouseRaid implements Runnable {

	private final Session _session;
	private volatile boolean run = true;

	public WarehouseRaid(Session session) {
		_session = session;
	}

	@Override
	public void run() {
		try {
			int numberOfOfficers = 1;
			Destination queue = _session.createQueue("Earth");
			Destination destination = _session
					.createTopic("VirtualTopic.Messages.Events.PoliceOfficerDied");

			while (run) {

				// Create a MessageProducer from the Session to the Topic or
				// Queue
				MessageProducer producer = _session.createProducer(destination);
				producer.setDeliveryMode(DeliveryMode.PERSISTENT);

				String message = String
						.format("<?xml version=\"1.0\" ?>"
								+ "<PoliceOfficerDied xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns=\"http://tempuri.net/Messages.Events\">"
								+ "<Identification>%s</Identification>"
								+ "<Name>Nick Walker (Native) the %sth</Name>"
								+ "</PoliceOfficerDied>", UUID.randomUUID()
								.toString(), numberOfOfficers++);

				TextMessage textMessage = _session.createTextMessage(message);
				producer.send(textMessage);

				Thread.sleep(5000);
			}

		} catch (Exception e) {
			System.err.println("WarehouseRaid thread caught exception: " + e);
			e.printStackTrace();
			System.exit(1);
		} finally {
			try {
				_session.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

	public void abort() {
		run = false;
	}

}
