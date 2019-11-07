package com.niit.tty.model;

public class TtyData {
	private String entryType;
    private String message;
    private String sessionId;
    
    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
    
    @Override
    public String toString() {
        return "TtyData{" +
                "entryType='" + entryType + '\'' +
                ", message='" + message + '\'' +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }

}
