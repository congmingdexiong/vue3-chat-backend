package com.zhuzhule.chatPigZhuzhuleBackend.domain.deepseek;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeepseekResult {

    String id;

    String created;

    Choice[] choices;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Choice[] getChoices() {
        return choices;
    }

    public void setChoices(Choice[] choices) {
        this.choices = choices;
    }
}





