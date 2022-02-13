package com.example.springbackendmoneytranfer.repository;

import com.example.springbackendmoneytranfer.log.Logger;
import com.example.springbackendmoneytranfer.model.Transfer;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.*;

@Data
@Repository
@AllArgsConstructor
public class TransferRepository {
    private static Map<String, Transfer> transfers = new LinkedHashMap<>();

    public String addTransferToList (Transfer transfer){
        String newOperationId = UUID.randomUUID().toString();
        transfer.setOperationId(newOperationId);
        transfers.put(newOperationId, transfer);
        return newOperationId;
    }

    public static Map<String, Transfer> getTransfers(){
        return transfers;
    }

    public static void removeTransferFromList(Transfer transfer){
        transfers.remove(transfer);
    }
}
