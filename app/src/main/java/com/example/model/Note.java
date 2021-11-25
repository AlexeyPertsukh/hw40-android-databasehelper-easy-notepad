package com.example.model;

import java.io.Serializable;

public class Note implements Serializable {
    public String title;
    public String memo;
    public String dt;

    public Note(String title, String memo, String dt) {
        this.title = title;
        this.memo = memo;
        this.dt = dt;
    }
}
