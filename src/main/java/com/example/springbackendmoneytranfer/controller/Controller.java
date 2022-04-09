package com.example.springbackendmoneytranfer.controller;

import com.example.springbackendmoneytranfer.model.Confirm;
import com.example.springbackendmoneytranfer.model.Transfer;
import com.example.springbackendmoneytranfer.service.ServiceTransfer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "https://serp-ya.github.io", maxAge = 3600)
@RequestMapping("/")
public class Controller {
    private ServiceTransfer service;

    public Controller(ServiceTransfer service) {
        this.service = service;
    }

    @RequestMapping(value = "transfer", method = RequestMethod.POST)
    public ResponseEntity<Object> postTransfer(@RequestBody Transfer transfer) {
        ResponseEntity<Object> responseEntity = service.addTransfer(transfer);
        return responseEntity;
    }

    @RequestMapping(value = "confirmOperation", method = RequestMethod.POST)
    public ResponseEntity<Object> confirmTransfer(@RequestBody Confirm confirmParam) {
        return service.confirmTransfer(confirmParam);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    Map<String, Object> handleRuntimeException(RuntimeException e) {
        return addExceptionMessage(e.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class, IOException.class, NullPointerException.class, MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, Object> handleIllegalArgumentException(Exception e) {
        return addExceptionMessage(e.getMessage());
    }

    private Map<String, Object> addExceptionMessage(String message) {
        Map<String, Object> answerObject = new HashMap<>();
        answerObject.put("id", 0);
        answerObject.put("message", message);
        return answerObject;
    }
}
