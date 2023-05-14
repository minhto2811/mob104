package com.example.mob104_app.Models;

import java.io.Serializable;

public class Banner implements Serializable {
    private final String image;
    private final String event;

    public Banner(String image, String event) {
        this.image = image;
        this.event = event;
    }

    public String getImage() {
        return image;
    }

    public String getEvent() {
        return event;
    }

    @Override
    public String toString() {
        return "Banner{" +
                "image='" + image + '\'' +
                ", event='" + event + '\'' +
                '}';
    }
}
