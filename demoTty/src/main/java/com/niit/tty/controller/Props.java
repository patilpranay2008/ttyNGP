package com.niit.tty.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;

public class Props {
	
	private String version;
	private Date zuluTimestamp;
	private List<String> teletypeDestination = new ArrayList<>();	
	
	public Props(String version,List list) {
		this.zuluTimestamp = new Date();
		this.version=version;
		this.teletypeDestination= list;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Date getZuluTimestamp() {
		return zuluTimestamp;
	}
	public void setZuluTimestamp(Date zuluTimestamp) {
		this.zuluTimestamp = zuluTimestamp;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return version+""+teletypeDestination;
	}
	
	 
	
	public static void main(String[] args) {
		List<String> list = new ArrayList<>();
		list.add("Pranayy");
		list.add("Pranayy1");
		Props prps = new Props("1.0",list);
		Props prps1 = new Props("2.0",list);
		
		System.out.println(prps);
			
		Gson gson = new Gson();
		String json = gson.toJson(prps);
		String json1 = gson.toJson(prps1);
		System.out.println(json);
		System.out.println(json1);

	}
	
	
	

}
