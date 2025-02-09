package com.zhuzhule.chatPigZhuzhuleBackend.domain.deepseek;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReasoningContent() {
        return reasoningContent;
    }

    public void setReasoningContent(String reasoningContent) {
        this.reasoningContent = reasoningContent;
    }

    String role;

    String content;

    @JsonProperty("reasoning_content")
    String reasoningContent;
}
