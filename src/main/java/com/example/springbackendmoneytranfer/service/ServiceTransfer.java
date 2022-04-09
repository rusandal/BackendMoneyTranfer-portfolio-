package com.example.springbackendmoneytranfer.service;

import com.example.springbackendmoneytranfer.model.Confirm;
import com.example.springbackendmoneytranfer.model.Transfer;
import com.example.springbackendmoneytranfer.repository.TransferRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@AllArgsConstructor
@Service
public class ServiceTransfer {
    private TransferRepository transferRepository;
    private static final Logger log = Logger.getLogger(ServiceTransfer.class);


    public ResponseEntity<Object> addTransfer(Transfer transfer) {
        String newOperationId = transferRepository.addTransferToMap(transfer);
        Map<String, Object> answerObject = new HashMap<>();
        if (transferRepository.getTransfers().containsKey(newOperationId)) {
            transfer.setStatusTransfer(Transfer.StatusTransfer.NEW);
            //transfer.setCode();
            log.info(transfer + " status transfer:NEW");
            //LoggerOld.logger(transfer, " ");
            answerObject.put("operationid", newOperationId);
            return ResponseEntity.ok().body(answerObject);
        } else {
            log.error("operationid:" + newOperationId + "is not added");
            throw new IllegalArgumentException();
        }
    }

    public ResponseEntity<Object> confirmTransfer(Confirm confirm) {
        /*Transfer transfer = transferRepository.getTransfers().get(confirm.getOperationId());
        if (transfer != null) {
            throw new NullPointerException("OperationId is invalid");
        } else if(confirm.getCode() != (transfer.getCode())){
        //Front Отдает только код 0000
        else */
        if (!confirm.getCode().equals("0000")) {
            /*Front не отдает transferID
            transfer.setStatusTransfer(Transfer.StatusTransfer.FAILEDTRANSFER);
            Logger.logger(transfer, "Code is invalid!");*/
            log.error("Bad confirm code");
            throw new IllegalArgumentException("Code is invalid!");
        }
        /*transfer.setStatusTransfer(Transfer.StatusTransfer.CONFIRMEDTRANSFER);
        Logger.logger(transfer, " ");
        Map<String, Object> answerObject = new HashMap<>();
        answerObject.put("operationid", transfer.getOperationId());
        transferRepository.removeTransferFromMap(transfer.getOperationId());
        return ResponseEntity.ok().body(answerObject);*/
        Map<String, Object> answerObject = new HashMap<>();
        answerObject.put("operationid", "this operation id");
        log.info("Transfer confirmed");
        return ResponseEntity.ok().body(answerObject);
    }
}
