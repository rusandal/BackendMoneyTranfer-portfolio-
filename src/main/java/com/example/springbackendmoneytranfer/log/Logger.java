package com.example.springbackendmoneytranfer.log;

import com.example.springbackendmoneytranfer.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Logger {
    private static final File FILE = new File("log.txt");

    public static void logger (Transfer transfer, String message){
        try (FileWriter fileWriter = new FileWriter(FILE, true)){
            if(transfer.getStatusTransfer().equals(Transfer.StatusTransfer.NEW))
            {
                fileWriter.write("NEW "+LocalDateTime.now()+" "+transfer.toString());
            } else if (transfer.getStatusTransfer().equals(Transfer.StatusTransfer.CONFIRMEDTRANSFER)){
                fileWriter.write("CONFIRMED "+LocalDateTime.now()+" "+transfer.toString());
            } else if (transfer.getStatusTransfer().equals(Transfer.StatusTransfer.FAILEDTRANSFER)){
                fileWriter.write("!!!FAILED "+message+" "+LocalDateTime.now()+" "+transfer.toString());
            } else {
                throw new IllegalArgumentException(transfer.getStatusTransfer().toString());
            }
            // запись по символам
            fileWriter.append('\n');
            fileWriter.append('!');
            // дозаписываем и очищаем буфер
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
