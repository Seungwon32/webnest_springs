package com.app.webnest.domain.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class WordExplanationRequestDTO {
    private String model;
    private List<WordAiMessageDTO> messages;
    private double temperature;

    public WordExplanationRequestDTO(String model, List<WordAiMessageDTO> messages) {
        this.model = model;
        this.messages = messages;
        this.temperature = 0.2; // 기본값
    }
}
