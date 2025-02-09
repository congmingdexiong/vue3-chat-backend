package com.zhuzhule.chatPigZhuzhuleBackend.domain.deepseek;


public class Choice{
    public Number getIndex() {
        return index;
    }

    public void setIndex(Number index) {
        this.index = index;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    Number index;

    Message message;
}