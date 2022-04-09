package com.example.springbackendmoneytranfer.model;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

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

    public enum StatusTransfer {
        NEW,
        CONFIRMEDTRANSFER,
        FAILEDTRANSFER
    }
}
