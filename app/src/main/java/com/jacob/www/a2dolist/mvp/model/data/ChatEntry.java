package com.jacob.www.a2dolist.mvp.model.data;

/**
 * Created by ASUS-NB on 2017/9/6.
 */

public class ChatEntry {
    private String text;

    public ChatEntry(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
