package ch.danielmarbach.activemq2nservicebus;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

public class PoliceOfficerDiedHandler implements MessageListener {

	private final Session session;

	public PoliceOfficerDiedHandler(Session session) {
		this.session = session;
	}

	@Override
	public void onMessage(Message message) {
		try {
			Extractor extractor = new Extractor((TextMessage) message);

			String officerName = extractor.extract("/PoliceOfficerDied/Name");
			String officerId = extractor
					.extract("/PoliceOfficerDied/Identification");

			System.out.println(String.format(
					"Police Officer %s will ascend to heaven.", officerName));

			String messageContent = String
					.format("<?xml version=\"1.0\" ?>"
							+ "<AscendToHeaven xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns=\"http://tempuri.net/Messages.Commands\">"
							+ "<PoliceOfficer>%s</PoliceOfficer>"
							+ "</AscendToHeaven>", officerId);

			Destination destination = session
					.createQueue(Constants.HEAVEN_QUEUE_NAME);
			MessageProducer producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);

			TextMessage textMessage = session.createTextMessage(messageContent);
			producer.send(textMessage);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
