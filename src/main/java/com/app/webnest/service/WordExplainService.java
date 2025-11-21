package com.app.webnest.service;

public interface WordExplainService {
//    단어가 들어오면, LLM에게 절의 후 단어 설명 문자열을 반환
    public String getWordExplanation(String word);
}
