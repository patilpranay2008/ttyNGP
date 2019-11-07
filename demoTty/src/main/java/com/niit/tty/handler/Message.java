package com.niit.tty.handler;




import java.util.ArrayList;
import java.util.List;

import com.niit.tty.model.TtyData;

public class Message {
    private String sessionId;
    private int id;
    private int intQDCount = 0;
    private int intQRCount = 0;
    private boolean receiverAdd = false;
    private boolean completeMessage = false;
    private String entries;
    private List<TtyData> ttyDataEntries = new ArrayList<TtyData>();
    //private Map <String, String> entries = new HashMap<>();

    public Message(String sessionId) {
        this.sessionId = sessionId;
    }


	public void incrementQDCount() { intQDCount++; }
    public void incrementQRCount() { intQRCount++; }
    public boolean maxQDCount() { return !(intQDCount > 32); }
    public boolean minQDCount() { return intQDCount > 0; }
    public boolean maxQRCount() { return !(intQRCount > 1); }
    public void reciverAdd() { receiverAdd = true; }
    public void completeMessage() { completeMessage = true; }

    public boolean isReceiverAdd() {
        return receiverAdd;
    }

    public boolean isCompleteMessage() {
        return completeMessage;
    }

    public String getEntries() {
        return entries;
    }

    public void saveEntry(TtyData ttyData) {
        if(entries == null)
            entries = "";
//        if(ttyDataEntries == null)
//        	this.ttyDataEntries = new ArrayList<TtyData>();
        this.entries = this.entries + "{'sessionId': "+sessionId+", 'entryType': "+ttyData.getEntryType()+", 'message': "+ttyData.getMessage()+"}\n";
        ttyDataEntries.add(ttyData);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {

        this.id = id;
    }


	public List<TtyData> getTtyDataEntries() {
		return ttyDataEntries;
	}


	public void setTtyDataEntries(List<TtyData> ttyDataEntries) {
		this.ttyDataEntries = ttyDataEntries;
	}


	public int getIntQDCount() {
		return intQDCount;
	}


	public void setIntQDCount(int intQDCount) {
		this.intQDCount = intQDCount;
	}
	

    

/*
	public void clearFlag(TtyData ttyData) {
    	
    	this.intQDCount = 0;
        this.intQRCount = 0;
        this.receiverAdd = false;
        this.completeMessage = false;
        
    }
*/
}