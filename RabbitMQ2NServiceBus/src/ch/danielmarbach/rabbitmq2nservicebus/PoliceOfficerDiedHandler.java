package ch.danielmarbach.rabbitmq2nservicebus;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;

public class PoliceOfficerDiedHandler implements HandleMessages {

	@Override
	public void handle(Channel channel, BasicProperties properties, byte[] body) {
		InputSource source = new InputSource();
		source.setCharacterStream(new StringReader(new String(body)));

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			Document document = db.parse(source);
			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();
			String officerName = (String) xpath.evaluate(
					"/PoliceOfficerDied/Name", document, XPathConstants.STRING);
			String officerId = (String) xpath.evaluate(
					"/PoliceOfficerDied/Identification", document,
					XPathConstants.STRING);

			System.out.println(String.format(
					"Police Officer %s will ascend to heaven.", officerName));

			Map<String, Object> headers = new HashMap<String, Object>();

			BasicProperties props = new AMQP.BasicProperties().builder()
					.messageId(UUID.randomUUID().toString())
					.contentType("text/xml").deliveryMode(2).headers(headers)
					.build();

			String message = String
					.format("<?xml version=\"1.0\" ?>"
							+ "<AscendToHeaven xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns=\"http://tempuri.net/Messages.Commands\">"
							+ "<PoliceOfficer>%s</PoliceOfficer>"
							+ "</AscendToHeaven>", officerId);

			channel.basicPublish("", Constants.HEAVEN_QUEUE_NAME, props,
					message.getBytes());
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
