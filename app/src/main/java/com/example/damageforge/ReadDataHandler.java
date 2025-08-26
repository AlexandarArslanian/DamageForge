package com.example.damageforge;

import android.os.Handler;

public class ReadDataHandler extends Handler {
    public String getJson() {
        return json;
    }
    public void setJson(String json) {
        this.json = json;
    }
    private String json;

}

