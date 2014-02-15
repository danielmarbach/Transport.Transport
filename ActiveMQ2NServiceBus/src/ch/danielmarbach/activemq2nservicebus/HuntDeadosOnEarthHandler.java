package ch.danielmarbach.activemq2nservicebus;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class HuntDeadosOnEarthHandler implements MessageListener {

	@Override
	public void onMessage(Message message) {
		Extractor extractor = new Extractor((TextMessage) message);

		String officerName = extractor.extract("/HuntDeadosOnEarth/Name");

		System.out.println(String.format(
				"Police Officer %s is back hunting deados!", officerName));
	}
}