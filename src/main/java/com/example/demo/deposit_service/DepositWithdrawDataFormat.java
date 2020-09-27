package com.example.demo.deposit_service;


import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class DepositWithdrawDataFormat {
    UUID depositId;
    UUID workerId;
    String withdrawDate;
}
