package com.niit.tty.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class XmlAddress {
	@XmlElement(name = "Priority")
	private String teletypePriority = "QK";
	
	@XmlElement(name = "Destination")
    private List<String> teletypeDestination = new ArrayList<>();
	
	public String getTeletypePriority() {
		return teletypePriority;
	}

	public void setTeletypePriority(String teletypePriority) {
		this.teletypePriority = teletypePriority;
	}

	public List<String> getTeletypeDestination() {
		return teletypeDestination;
	}

	public void setTeletypeDestination(List<String> teletypeDestination) {
		this.teletypeDestination = teletypeDestination;
	}
	
	public void addDestination(String destination) {
		this.teletypeDestination.add(destination);
	}

}
