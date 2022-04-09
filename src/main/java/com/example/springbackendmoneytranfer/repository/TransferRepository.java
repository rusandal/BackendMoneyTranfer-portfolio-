package com.example.springbackendmoneytranfer.repository;

import com.example.springbackendmoneytranfer.model.Transfer;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Repository
@AllArgsConstructor
public class TransferRepository {
    private Map<String, Transfer> transfers = new ConcurrentHashMap<>();

    public String addTransferToMap(Transfer transfer) {
        String newOperationId = UUID.randomUUID().toString();
        transfer.setOperationId(newOperationId);
        transfers.put(newOperationId, transfer);
        return newOperationId;
    }

    public Map<String, Transfer> getTransfers() {
        return transfers;
    }

    public void removeTransferFromMap(String operationId) {
        transfers.remove(operationId);
    }
}
