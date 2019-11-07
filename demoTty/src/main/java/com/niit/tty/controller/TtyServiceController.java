package com.niit.tty.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import com.niit.tty.model.TtyData;
import com.niit.tty.model.MessageOutput;
import com.niit.tty.service.TtyService;
import com.niit.tty.util.CustomErrorType;
import com.niit.tty.util.ValidationErrorMessage;
// Final
@RestController
@RequestMapping("/api")
public class TtyServiceController {

    public static final Logger logger = LoggerFactory.getLogger(TtyServiceController.class);

    @Autowired
    TtyService ttyService; //Service which will do all data retrieval/manipulation work
    @PostMapping(value = "/tty")
    @RequestMapping(value = "/tty", method = RequestMethod.POST)
    public ResponseEntity<?> postData(@RequestBody TtyData ttyData, UriComponentsBuilder ucBuilder) {
        logger.info("Reading Input Stream", ttyData.toString());
        
        ValidationErrorMessage validationErrorMessage =ttyService.validateData(ttyData);
        if (validationErrorMessage.getErrorStatus()) {
            logger.error("Invalid Data");
            return new ResponseEntity(new CustomErrorType(validationErrorMessage.getErrorMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ttyService.saveData(ttyData);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/user/{id}").buildAndExpand(ttyData.getSessionId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    @GetMapping(value = "/tty")
    @RequestMapping(value = "/tty", method = RequestMethod.GET)
    public String get(@RequestParam(value="id", required = false) String id,
                      @RequestParam(value="sessionId", required = false) String sessionId ) {
        if (id != null )
    	    return ttyService.getMessage(Integer.parseInt(id));
        else if (sessionId != null)
            return ttyService.getMessageBySession(Integer.parseInt(sessionId));
        return "";
    }
    
}