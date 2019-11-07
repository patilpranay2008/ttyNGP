package com.niit.tty.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Message")
@XmlAccessorType(XmlAccessType.FIELD)

public class XmlMessage {
	@XmlElement(name = "Timestamp")
	private Date zuluTimestamp;
	
	@XmlElement(name = "MessageIdentity")
	private String messageIdentity;
	
	@XmlElement(name = "MessageType")
	private String messageType;
	
	
	@XmlElement(name = "AddressLine")
    private XmlAddress xmlAddress;
	
	@XmlElement(name="Origin")
	private String teletypeOrigin;
	
	@XmlElement(name = "MessageBody")
    private XmlMessageBody xmlMessageBody;

	public Date getZuluTimestamp() {
		return zuluTimestamp;
	}

	public void setZuluTimestamp(Date zuluTimestamp) {
		this.zuluTimestamp = zuluTimestamp;
	}

	public XmlAddress getAddress() {
		return xmlAddress;
	}

	public void setAddress(XmlAddress xmlAddress) {
		this.xmlAddress = xmlAddress;
	}

	public String getTeletypeOrigin() {
		return teletypeOrigin;
	}

	public void setTeletypeOrigin(String teletypeOrigin) {
		this.teletypeOrigin = teletypeOrigin;
	}

	public XmlMessageBody getMessageBody() {
		return xmlMessageBody;
	}

	public void setMessageBody(XmlMessageBody xmlMessageBody) {
		this.xmlMessageBody = xmlMessageBody;
	}

	public String getMessageIdentity() {
		return messageIdentity;
	}

	public void setMessageIdentity(String messageIdentity) {
		this.messageIdentity = messageIdentity;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	

}
