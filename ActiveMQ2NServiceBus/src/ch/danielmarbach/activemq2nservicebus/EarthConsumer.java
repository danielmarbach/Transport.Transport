package ch.danielmarbach.activemq2nservicebus;

import java.util.ArrayList;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;

public class EarthConsumer {

	private final ArrayList<MessageConsumer> _consumers;
	private final Session _session;

	public EarthConsumer(Session session) {
		_session = session;
		_consumers = new ArrayList<MessageConsumer>();
	}

	public void start() throws JMSException {
		Queue policeOfficerDiedQueue = _session
				.createQueue("Consumer.Earth.VirtualTopic.Messages.Events.PoliceOfficerDied");

		MessageConsumer policeOfficerDiedConsumer = _session
				.createConsumer(policeOfficerDiedQueue);
		policeOfficerDiedConsumer
				.setMessageListener(new PoliceOfficerDiedHandler());

		_consumers.add(policeOfficerDiedConsumer);
	}

	public void stop() throws JMSException {
		for (MessageConsumer consumer : _consumers) {
			try {
				consumer.close();
			} catch (JMSException exception) {
				exception.printStackTrace();
			}
		}
	}
}
