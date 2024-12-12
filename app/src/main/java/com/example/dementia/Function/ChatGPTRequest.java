package com.example.dementia.Function;

import java.util.List;

public class ChatGPTRequest {
    private String model;
    private List<Messages> messages;

    public ChatGPTRequest(String model, List<Messages> messages) {
        this.model = model;
        this.messages = messages;
    }

    // 내부 클래스: 메시지 구조 정의
    public static class Messages {
        private String role;
        private String content;

        public Messages(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}

