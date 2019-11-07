package com.niit.tty.service;

import com.niit.tty.handler.Message;
import com.niit.tty.model.TtyData;
import com.niit.tty.util.ValidationErrorMessage;

public interface TtyService {
	ValidationErrorMessage validateData(TtyData ttyData);

    void saveData(TtyData ttyData);
    
    String getMessage(int id);
    
    String getMessageBySession(int sessionId);

	void sendData(Message message);
    
}

