package com.niit.tty.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class XmlMessageBody {
	@XmlElement(name = "List")
	private List<String> teletypeText = new ArrayList<>();

	public List<String> getTeletypeText() {
		return teletypeText;
	}

	public void setTeletypeText(List<String> teletypeText) {
		this.teletypeText = teletypeText;
	}
	
	public void addTeleTypeText(String text) {
		this.teletypeText.add(text);
	}

}
