package com.example.dementia.Function;

import java.util.List;

public class ChatGPTResponse {
    private List<Choice> choices;

    public List<Choice> getChoices(){
        return choices;
    }

    public static class Choice{
        private Messages message;

        public Messages getMessage(){
            return message;
        }

        public static class Messages{
            private String content;
            public String getContent(){
                return content;
            }
        }
    }

}
