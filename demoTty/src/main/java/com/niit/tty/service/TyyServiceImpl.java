package com.niit.tty.service;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.json.JSONException;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.niit.tty.handler.Message;
import com.niit.tty.jms.JmsPutGet;
import com.niit.tty.model.MessageOutput;
import com.niit.tty.model.TtyData;
import com.niit.tty.model.XmlAddress;
import com.niit.tty.model.XmlMessage;
import com.niit.tty.model.XmlMessageBody;
import com.niit.tty.util.ValidationErrorMessage;
import org.json.JSONObject;


import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.TextMessage;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import com.niit.tty.service.TyyServiceImpl;


@Service("ttyService")
public class TyyServiceImpl implements TtyService {

	public static final Logger logger = LoggerFactory.getLogger(TyyServiceImpl.class);
	Map<String, Message> messages = new HashMap<>();
	Map<Integer, Message> previousMessages = new HashMap();
	String enterType = "";
	public int messageCounter = 1;
	
	
	// System exit status value (assume unset value to be 1)
	private static int status = 1;
	// Create variables for the connection to MQ
	private static final String HOST = "10.14.242.21"; // Host name or IP address
	private static final int PORT = 2200; // Listener port for your queue manager
	private static final String CHANNEL = "CHL.QMO00"; // Channel name
	private static final String QMGR = "QMO00"; // Queue manager name
	private static final String APP_USER = "app"; // User name that application uses to connect to MQ
	private static final String APP_PASSWORD = "_APP_PASSWORD_"; // Password that the application uses to connect to MQ
	private static final String QUEUE_NAME = "Q.EDFY.EM.OUTPUT.XML"; // Queue that the application uses to put and get messages to and from



	@Override
	public ValidationErrorMessage validateData(TtyData ttyData) {
		Message message = messages.get(ttyData.getSessionId());
		if (message == null) {
			message = new Message(ttyData.getSessionId());
			messages.put(ttyData.getSessionId(), message);
		}
		boolean result = false;
		enterType = ttyData.getEntryType().trim().toUpperCase();
		// switch (ttyData.getEntryType().trim().toUpperCase()) {
		switch (enterType) {

		case "Y'QD":
		case "Y'QP":
		case "Y'QK":
			result = message.minQDCount();
			if (result)
				return new ValidationErrorMessage("INVALID ADDRESS FIELD");
			String[] values = ttyData.getMessage().split(" ");
			if (values.length <= 0)
				return new ValidationErrorMessage("INVALID DATA ENTERED CHECK INPUT");
			for (String value : values) {
				if (!(value.length() == 7))
					return new ValidationErrorMessage("INVALID DATA ENTERED CHECK INPUT");
				message.incrementQDCount();
			}
			result = message.maxQDCount();
			if (!result)
				return new ValidationErrorMessage("INVALID DATA ENTERED CHECK INPUT");
			break;

		case "Y'.":
			result = message.minQDCount();
			if (!result)
				return new ValidationErrorMessage("INVALID DATA ENTERED CHECK INPUT");
			if (!(ttyData.getMessage().length() == 7))
				return new ValidationErrorMessage("ONLY ONE SIGNATURE ALLOWED");
			message.incrementQRCount();
			result = message.maxQRCount();
			if (!result)
				return new ValidationErrorMessage("ONLY ONE SIGNATURE ALLOWED");
			message.reciverAdd();
			break;

		case "Y'":
			if (!message.isReceiverAdd()) {
				String[] values1 = ttyData.getMessage().split(" ");
				if (values1.length <= 0)
					return new ValidationErrorMessage("INVALID DATA ENTERED CHECK INPUT ");
				for (String value : values1) {
					if (!(value.length() == 7))
						return new ValidationErrorMessage("INVALID DATA ENTERED CHECK INPUT");
					message.incrementQDCount();
				}
				result = message.maxQDCount();
				if (!result)
					return new ValidationErrorMessage("INVALID DATA ENTERED CHECK INPUT");
				enterType = "Y'QK";
				ttyData.setEntryType(enterType);
				break;
			}

			// PPNEW return new ValidationErrorMessage("INVALID DATA ENTERED CHECK INPUT ");
			if (ttyData.getMessage().length() <= 0)
				return new ValidationErrorMessage("#FORMAT#");
			message.completeMessage();
			break;

		case "YET":
			result = message.minQDCount();
			if (!result)
				return new ValidationErrorMessage("NO MSGS");
			if (!message.isReceiverAdd())
				return new ValidationErrorMessage("INVALID ADDRESSEE ");
			if (!message.isCompleteMessage())
				return new ValidationErrorMessage("INVALID TEXT FIELD ");
			break;

		case "YIG":
			result = message.minQDCount();
			if (!result)
				return new ValidationErrorMessage("NO MSGS");
			break;

		default:
			return new ValidationErrorMessage("NO ENTRY TYPE/CRITERIA MATCHED");
		}
		return new ValidationErrorMessage("NO ERROR", false);
	}

	@Override
	public void saveData(TtyData ttyData) {
		if (ttyData.getEntryType().equalsIgnoreCase("YIG")) // clear previous entries
		{
			Message message = messages.get(ttyData.getSessionId());
			clearMessage(ttyData.getSessionId());

		} else if (ttyData.getEntryType().equalsIgnoreCase("YET")) { // end of entries in message
			Message message = messages.get(ttyData.getSessionId());
			message.saveEntry(ttyData);
			displayEntriesToConsole(messageCounter, message.getEntries());
			sendData(message);
			storeMessage(ttyData.getSessionId());
		} else {
			messages.get(ttyData.getSessionId()).saveEntry(ttyData);
		}
	}

	private void clearMessage(String sessionId) {
		Message message = messages.get(sessionId);
		messages.remove(sessionId);
		System.out.println("YIG");
	}

	private void storeMessage(String sessionId) {
		Message message = messages.get(sessionId);
		message.setId(messageCounter);
		previousMessages.put(message.getId(), message);
		messageCounter++;
		messages.remove(sessionId); // Reset the entries for new message's entries
	}

	private String displayEntriesToConsole(int id, String entries) {
		String message = "{'id':" + id + ", message:[" + entries + "]}";
		logger.info(message);
		return message;
	}

	@Override
	public String getMessage(int id) {
		String entries = previousMessages.get(id).getEntries();
		if (entries != null) {
			return displayEntriesToConsole(id, entries);
		}
		return "NO MESSAGE FOUND WITH ID " + id;
	}

	@Override
	public String getMessageBySession(int sessionId) {
		Message message = messages.get(sessionId + "");
		if (message != null) {
			logger.info(message.getEntries());
			return message.getEntries();
		}
		logger.error("NO MESSAGE FOUND WITH ID " + sessionId);
		return "NO MESSAGE FOUND WITH ID " + sessionId;
	}

	@Override
	public void sendData(Message message) {
		MessageOutput messageOutput = new MessageOutput();
		JmsPutGet  jmsPutGet = new JmsPutGet();
		XmlMessage xmlMessage = new XmlMessage();
		XmlAddress xmlAddress = new XmlAddress();
		XmlMessageBody xmlMessageBody = new XmlMessageBody();
		messageOutput.setMessageType("TMSG");
		messageOutput.setVersion("0.1");
		xmlMessage.setZuluTimestamp(new Date());
//		xmlMessage.setMessageIdentity(messageIdentity);
		xmlMessage.setMessageType("XML");
//		messageOutput.setZuluTimestamp(new Date());
	
		messageOutput.setTeletypeType("MSW");
		if (message.getIntQDCount() > 1)
			messageOutput.setTeletypeSendto(true);
		for (TtyData ttyData : message.getTtyDataEntries()) {
			if (ttyData.getEntryType().equalsIgnoreCase("Y'QD")) {
				xmlAddress.setTeletypePriority("QD");
//				messageOutput.setTeletypePriority("QD");
				String[] values = ttyData.getMessage().split(" ");
				for (String value : values)
					xmlAddress.addDestination(value);
					xmlMessage.setAddress(xmlAddress);
//					messageOutput.addDestination(value);
			}
			if (ttyData.getEntryType().equalsIgnoreCase("Y'QP")) {
				xmlAddress.setTeletypePriority("QP");
//				messageOutput.setTeletypePriority("QP");
				String[] values = ttyData.getMessage().split(" ");
				for (String value : values)
					xmlAddress.addDestination(value);
					xmlMessage.setAddress(xmlAddress);
//					messageOutput.addDestination(value);
			}
			if (ttyData.getEntryType().equalsIgnoreCase("Y'QK")) {
				String[] values = ttyData.getMessage().split(" ");
				for (String value : values)
					xmlAddress.addDestination(value);
				    xmlMessage.setAddress(xmlAddress);
//					messageOutput.addDestination(value);
			}

			if (ttyData.getEntryType().equalsIgnoreCase("Y'.")) {
//				messageOutput.setTeletypeOrigin(ttyData.getMessage());
				xmlMessage.setTeletypeOrigin(ttyData.getMessage());
				messageOutput.setTeletypePartition(ttyData.getMessage().substring(5, 7));
			}

			if (ttyData.getEntryType().equalsIgnoreCase("Y'")) {
				xmlMessageBody.addTeleTypeText(ttyData.getMessage());
				xmlMessage.setMessageBody(xmlMessageBody);
//				messageOutput.addTeleTypeText(ttyData.getMessage());
			}

		}
		messageOutput.setXmlMessage(xmlMessage);
		Gson gson = new Gson();
		System.out.println(gson.toJson(messageOutput));
		try {
//		MessageOutput messageOutput1 = unmarshalling();
//		marshalling(messageOutput);
		marshalling(xmlMessage);		
		} catch (JAXBException e) {
            e.printStackTrace();
        }
	}
/*	
	public static void cnvrtJsonSt(String json2xml) {
		  String json_value = json2xml; 
	        System.out.println(convert_json(json2xml));

	    }
	    public static String convert_json(String json_value) {
	        String xml = "";
	        try {
	            JSONObject jsoObject = new JSONObject(json_value);
	            xml = xml + XML.toString(jsoObject);
	        } catch (Exception e) {
	            System.out.println(e);
	        }
	        xml = xml + "";
	        return xml;
	    }
*/	    
	 public void marshalling(XmlMessage messageOutput1) throws JAXBException
	 {	
		JAXBContext jaxbContext = JAXBContext.newInstance(XmlMessage.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		StringWriter sw = new StringWriter();
		jaxbMarshaller.marshal(messageOutput1, sw);
		String xmlContent = sw.toString();
		System.out.println(xmlContent);
		logger.info(xmlContent);
		dataCheck(xmlContent);
		
	}
	 private static XmlMessage unmarshalling() throws JAXBException, IOException {
	        JAXBContext jaxbContext = JAXBContext.newInstance(XmlMessage.class);
	        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
	        File file = new File("src/main/resources/complex.xml");
	        XmlMessage messageOutput1 = (XmlMessage) unmarshaller.unmarshal(file);
	        if(messageOutput1 != null) {
	            System.out.println("Unmarshalling successful!");
	        }
	        return messageOutput1;
	    }
	 
	 public void dataCheck(String xmlContent)
	 {
		 System.out.println("XML Check...." +xmlContent);
		// Variables
			JMSContext context = null;
			Destination destination = null;
			JMSProducer producer = null;
			JMSConsumer consumer = null;



			try {
				// Create a connection factory
				JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
				JmsConnectionFactory cf = ff.createConnectionFactory();

				// Set the properties
				cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, HOST);
				cf.setIntProperty(WMQConstants.WMQ_PORT, PORT);
				cf.setStringProperty(WMQConstants.WMQ_CHANNEL, CHANNEL);
				cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
				cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, QMGR);
				cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, "JmsPutGet (JMS)");
				cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
				cf.setStringProperty(WMQConstants.USERID, APP_USER);
				cf.setStringProperty(WMQConstants.PASSWORD, APP_PASSWORD);

				// Create JMS objects
				context = cf.createContext();
				destination = context.createQueue("queue:///" + QUEUE_NAME);

				TextMessage message = context.createTextMessage(xmlContent);
	 
				producer = context.createProducer();
				producer.send(destination, message);
				System.out.println("Sent message:\n" + message);

				consumer = context.createConsumer(destination); // autoclosable
				String receivedMessage = consumer.receiveBody(String.class, 15000); // in ms or 15 seconds

				System.out.println("\nReceived message:\n" + receivedMessage);

				recordSuccess();
			} catch (JMSException jmsex) {
				recordFailure(jmsex);
			}

			System.exit(status);


		 
	 }
	 
	 private static void recordSuccess() {
			System.out.println("SUCCESS");
			status = 0;
			return;
		}
	 
	 private static void recordFailure(Exception ex) {
		 System.out.println("failure....");
			if (ex != null) {
				if (ex instanceof JMSException) {
					processJMSException((JMSException) ex);
				} else {
					System.out.println(ex);
				}
			}
			System.out.println("FAILURE");
			status = -1;
			return;
		}
	 
	 private static void processJMSException(JMSException jmsex) {
			System.out.println(jmsex);
			Throwable innerException = jmsex.getLinkedException();
			if (innerException != null) {
				System.out.println("Inner exception(s):");
			}
			while (innerException != null) {
				System.out.println(innerException);
				innerException = innerException.getCause();
			}
			return;
	}
	 
}
