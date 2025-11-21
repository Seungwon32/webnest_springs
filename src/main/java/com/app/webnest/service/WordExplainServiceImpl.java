package com.app.webnest.service;

import com.app.webnest.domain.dto.WordAiMessageDTO;
import com.app.webnest.domain.dto.WordExplanationRequestDTO;
import com.app.webnest.domain.dto.WordExplanationResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class WordExplainServiceImpl implements WordExplainService {

    private final WebClient openAiWebClient;
    private final String model;

    // 같은 단어 여러 번 나올 때 재호출 줄이기 위한 캐시
    private final Map<String, String> cache = new ConcurrentHashMap<>();

    public WordExplainServiceImpl(WebClient openAiWebClient,
                                  @Value("${openai.model}") String model) {
        this.openAiWebClient = openAiWebClient;
        this.model = model;
    }

    @Override
    public String getWordExplanation(String word) {
        // 0) 캐시 먼저 확인
        String cached = cache.get(word);
        if (cached != null) {
            log.debug("[LLM] 캐시 히트 word={}, explanation={}", word, cached);
            return cached;
        }

        // system role: 역할 정의
        String systemPrompt = """
                너는 한국어 사전이야.
                사용자가 단어를 보내면 그 단어의 뜻을 한국어로 짧고 명확하게 설명해줘.
                위키/백과사전식 긴 설명 말고, 끝말잇기 게임에서 보여줄 정도의 간단한 정의만 4~5문장으로.
                추가 멘트는 절대 하지 마. (예: "도움이 더 필요하면 말해" 같은 문장 금지)
                """;

        // user role: 실제 단어 입력
        String userPrompt = "단어: \"" + word + "\"\n해당 단어의 뜻을 설명해줘.";

        WordExplanationRequestDTO request = new WordExplanationRequestDTO(
                model,
                List.of(
                        new WordAiMessageDTO("system", systemPrompt),
                        new WordAiMessageDTO("user", userPrompt)
                )
        );

        // 1) 최대 3번까지 재시도
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                log.info("[LLM] 요청 word={}, attempt={}, request={}", word, attempt, request);

                WordExplanationResponseDTO response = openAiWebClient.post()
                        .uri("/chat/completions")
                        .bodyValue(request)
                        .retrieve()
                        .bodyToMono(WordExplanationResponseDTO.class)
                        .block(); // 동기 호출

                log.info("[LLM] 응답 raw={}", response);

                if (response == null ||
                        response.getChoices() == null ||
                        response.getChoices().isEmpty() ||
                        response.getChoices().get(0).getMessage() == null ||
                        response.getChoices().get(0).getMessage().getContent() == null) {

                    log.error("[LLM] 응답 비정상: {}", response);
                    continue; // 다음 재시도
                }

                String explanation = response.getChoices()
                        .get(0)
                        .getMessage()
                        .getContent()
                        .trim();

                cache.put(word, explanation); // 캐시에 저장
                log.info("[LLM] 최종 explanation={}", explanation);
                return explanation;

            } catch (WebClientResponseException e) {
                int status = e.getRawStatusCode();
                String body = e.getResponseBodyAsString();
                log.warn("[LLM] 응답 에러 status={}, body={}, attempt={}", status, body, attempt);

                // 429(레이트 리밋) / 5xx(서버 오류)만 재시도, 나머지는 바로 종료
                if (status == 429 || (status >= 500 && status < 600)) {
                    sleepBackoff(attempt);
                    continue;
                } else {
                    break;
                }
            } catch (Exception e) {
                // 네트워크/타임아웃 등 기타 오류
                log.error("[LLM] 호출 오류 attempt={}", attempt, e);
                sleepBackoff(attempt);
            }
        }

        log.error("[LLM] 모든 재시도 실패, 기본 문구 반환 word={}", word);
        return "단어 설명을 가져오지 못했습니다.";
    }

    private void sleepBackoff(int attempt) {
        try {
            Thread.sleep(200L * attempt); // 0.2초, 0.4초, 0.6초
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}