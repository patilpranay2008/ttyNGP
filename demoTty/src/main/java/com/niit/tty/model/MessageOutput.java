package com.niit.tty.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MessageOutput {	
//  private String teletypePriority = "QK";
//	private List<String> teletypeDestination = new ArrayList<>();
	private String messageType;
	private String version;
//	private Date zuluTimestamp;
	private String teletypeType;
//	private String teletypeOrigin;
	private String teletypePartition;
	private String teletypeTag;
	private boolean teletypeSendto = false;
	private boolean teletypeSplit = true;
	private XmlMessage xmlMessage;
//	private List<String> teletypeText = new ArrayList<>();
	
	
	
	public String getMessageType() {
		return messageType;
	}
	public XmlMessage getXmlMessage() {
		return xmlMessage;
	}
	public void setXmlMessage(XmlMessage xmlMessage) {
		this.xmlMessage = xmlMessage;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
//	public Date getZuluTimestamp() {
//		return zuluTimestamp;
//	}
//	public void setZuluTimestamp(Date zuluTimestamp) {
//		this.zuluTimestamp = zuluTimestamp;
//	}
	public String getTeletypeType() {
		return teletypeType;
	}
	public void setTeletypeType(String teletypeType) {
		this.teletypeType = teletypeType;
	}
//	public String getTeletypePriority() {
//		return teletypePriority;
//	}
//	public void setTeletypePriority(String teletypePriority) {
//		this.teletypePriority = teletypePriority;
//	}
	
	public String getTeletypePartition() {
		return teletypePartition;
	}
	public void setTeletypePartition(String teletypePartition) {
		this.teletypePartition = teletypePartition;
	}
	public String getTeletypeTag() {
		return teletypeTag;
	}
	public void setTeletypeTag(String teletypeTag) {
		this.teletypeTag = teletypeTag;
	}
	public boolean isTeletypeSendto() {
		return teletypeSendto;
	}
	public void setTeletypeSendto(boolean teletypeSendto) {
		this.teletypeSendto = teletypeSendto;
	}
	public boolean isTeletypeSplit() {
		return teletypeSplit;
	}
	public void setTeletypeSplit(boolean teletypeSplit) {
		this.teletypeSplit = teletypeSplit;
	}
//	public List<String> getTeletypeText() {
//		return teletypeText;
//	}
//	public void setTeletypeText(List<String> teletypeText) {
//		this.teletypeText = teletypeText;
//	}
//	public String getTeletypeOrigin() {
//		return teletypeOrigin;
//	}
//	public void setTeletypeOrigin(String teletypeOrigin) {
//		this.teletypeOrigin = teletypeOrigin;
//	}
//	public List<String> getTeletypeDestination() {
//		return teletypeDestination;
//	}
//	public void setTeletypeDestination(List<String> teletypeDestination) {
//		this.teletypeDestination = teletypeDestination;
//	}

//	public void addDestination(String destination) {
//		this.teletypeDestination.add(destination);
//	}
	
//	public void addTeleTypeText(String text) {
//		this.teletypeText.add(text);
//	}


}
