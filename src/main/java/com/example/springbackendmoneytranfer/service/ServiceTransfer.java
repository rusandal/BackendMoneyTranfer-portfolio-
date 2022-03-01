package com.example.springbackendmoneytranfer.service;

import com.example.springbackendmoneytranfer.log.Logger;
import com.example.springbackendmoneytranfer.model.Confirm;
import com.example.springbackendmoneytranfer.model.Transfer;
import com.example.springbackendmoneytranfer.repository.TransferRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@Service
public class ServiceTransfer {
    private TransferRepository transferRepository;


    public ResponseEntity<Object> addTransfer(Transfer transfer) {
        String newOperationId = transferRepository.addTransferToList(transfer);
        Map<String, Object> answerObject = new HashMap<>();
        if (!transferRepository.getTransfers().containsKey(newOperationId)) {
            transfer.setStatusTransfer(Transfer.StatusTransfer.NEW);
            //transfer.setCode();
            Logger.logger(transfer, " ");
            answerObject.put("operationid", newOperationId);
            return ResponseEntity.ok().body(answerObject);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public ResponseEntity<Object> confirmTransfer(Confirm confirm) {
        //Сервис перестал отдавать transferId, пришлось закомитить
        //Transfer transfer = transferRepository.getTransfers().get(confirm.getOperationId());
        /*if (transfer != null) {
            throw new NullPointerException("OperationId is invalid");
        } //else if(confirm.getCode() != (transfer.getCode())){
        //Отдает только код 0000
        else */if(!confirm.getCode().equals("0000")){
            //transfer.setStatusTransfer(Transfer.StatusTransfer.FAILEDTRANSFER);
            //Logger.logger(transfer, "Code is invalid!");
            throw new IllegalArgumentException("Code is invalid!");
        }
        //transfer.setStatusTransfer(Transfer.StatusTransfer.CONFIRMEDTRANSFER);
        //Logger.logger(transfer, " ");
        //Map<String, Object> answerObject = new HashMap<>();
        //answerObject.put("operationid", transfer.getOperationId());
        //transferRepository.removeTransferFromMap(transfer.getOperationId());
        //return ResponseEntity.ok().body(answerObject);
        Map<String, Object> answerObject = new HashMap<>();
        answerObject.put("operationid", "this operation id");

        return ResponseEntity.ok().body(answerObject);
    }
}
