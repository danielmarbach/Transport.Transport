package ch.danielmarbach.activemq2nservicebus;

import javax.jms.Message;
import javax.jms.MessageListener;

public class PoliceOfficerDiedHandler implements MessageListener {

	@Override
	public void onMessage(Message message) {
		System.out.println(message);
	}

}
