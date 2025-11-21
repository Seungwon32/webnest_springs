package com.app.webnest.domain.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class WordAiMessageDTO {
    private String role;    // "system" | "user"
    private String content;
}
