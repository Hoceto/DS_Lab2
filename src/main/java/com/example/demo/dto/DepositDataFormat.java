package com.example.demo.dto;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;

import javax.persistence.JoinColumn;
import java.util.UUID;

@Data
@Builder
public class DepositDataFormat {
    @NotNull
    int balance;

    @NotNull
    String openingDate;

    @NotNull
    UUID ownerId;

    @NotNull
    UUID workerId;
}
