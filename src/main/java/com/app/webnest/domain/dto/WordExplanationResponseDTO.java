package com.app.webnest.domain.dto;
import com.app.webnest.service.TypingContentsService;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class WordExplanationResponseDTO {
    private List<Choice> choices;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class Choice {
        private WordAiMessageDTO message;
    }
}
