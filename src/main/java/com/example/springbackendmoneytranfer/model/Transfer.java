package com.example.springbackendmoneytranfer.model;

import lombok.Data;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Random;

@Data
@Validated
public class Transfer {
    public static final int MIN_CODE = 1000;
    public static final int MAX_CODE = 9999;
    private String cardFromNumber;
    private String cardFromValidTill;
    private String cardFromCVV;
    private String cardToNumber;
    private Amount amount;
    private String operationId;
    private StatusTransfer statusTransfer;
    private String code;

    @Data
    public class Amount {
        private Integer value;
        private String currency;
    }

    public enum StatusTransfer{
        NEW,
        CONFIRMEDTRANSFER,
        FAILEDTRANSFER
    }

    /*public void setCode(){
        Random random = new Random();
        this.code = random.nextInt(MAX_CODE - MIN_CODE) + MIN_CODE;
    }*/
}
