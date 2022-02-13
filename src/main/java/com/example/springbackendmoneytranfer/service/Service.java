package com.example.springbackendmoneytranfer.service;

import com.example.springbackendmoneytranfer.log.Logger;
import com.example.springbackendmoneytranfer.model.Confirm;
import com.example.springbackendmoneytranfer.model.Transfer;
import com.example.springbackendmoneytranfer.repository.TransferRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@org.springframework.stereotype.Service
public class Service {
    private TransferRepository transferRepository;


    public ResponseEntity<Object> addTransfer(Transfer transfer) {
        String newOperationId = transferRepository.addTransferToList(transfer);
        Map<String, Object> answerObject = new HashMap<>();
        if (TransferRepository.getTransfers().containsKey(newOperationId)) {
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
        Transfer transfer = TransferRepository.getTransfers().get(confirm.getOperationId());
        if (transfer == null) {
            throw new NullPointerException("OperationId is invalid");
        } else if(confirm.getCode() != (transfer.getCode())){
            transfer.setStatusTransfer(Transfer.StatusTransfer.FAILEDTRANSFER);
            Logger.logger(transfer, "Code is invalid!");
            throw new IllegalArgumentException("Code is invalid!");
        }
        transfer.setStatusTransfer(Transfer.StatusTransfer.CONFIRMEDTRANSFER);
        Logger.logger(transfer, " ");
        Map<String, Object> answerObject = new HashMap<>();
        answerObject.put("operationid", transfer.getOperationId());
        return ResponseEntity.ok().body(answerObject);
    }
}
