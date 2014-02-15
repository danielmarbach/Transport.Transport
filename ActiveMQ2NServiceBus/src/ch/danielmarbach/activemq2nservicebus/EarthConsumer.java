package ch.danielmarbach.activemq2nservicebus;

import java.util.ArrayList;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;

public class EarthConsumer {

	private final ArrayList<MessageConsumer> consumers;
	private final Session session;

	public EarthConsumer(Session session) {
		this.session = session;
		consumers = new ArrayList<MessageConsumer>();
	}

	public void start() throws JMSException {
		Queue policeOfficerDiedQueue = session.createQueue(String.format(
				"Consumer.%s.%s", Constants.QUEUE_NAME, Constants.TOPIC_NAME));

		MessageConsumer policeOfficerDiedConsumer = session
				.createConsumer(policeOfficerDiedQueue);
		policeOfficerDiedConsumer
				.setMessageListener(new PoliceOfficerDiedHandler(session));

		consumers.add(policeOfficerDiedConsumer);

		Destination huntDeadosOnEarth = session
				.createQueue(Constants.QUEUE_NAME);

		MessageConsumer huntDeadosOnEarthConsumer = session
				.createConsumer(huntDeadosOnEarth);
		huntDeadosOnEarthConsumer
				.setMessageListener(new HuntDeadosOnEarthHandler());

		consumers.add(huntDeadosOnEarthConsumer);
	}

	public void stop() {
		for (MessageConsumer consumer : consumers) {
			try {
				consumer.close();
			} catch (JMSException exception) {
				exception.printStackTrace();
			}
		}
	}
}
