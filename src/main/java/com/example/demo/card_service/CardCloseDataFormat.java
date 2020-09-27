package com.example.demo.card_service;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CardCloseDataFormat {
    UUID cardId;
}
