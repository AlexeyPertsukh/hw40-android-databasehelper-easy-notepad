package com.example.hw40_notebook;

import java.util.ArrayList;
import java.util.List;

public class DefaultItems {
    private DefaultItems() {
    }

    public static List<Note> getNotes() {
        List<Note> list = new ArrayList<>();
        list.add(new Note("note1", "note test1", "2021-05-10 11:02"));
        list.add(new Note("note2", "note test2", "2021-11-10 10:05"));
        list.add(new Note("one more note3", "note test3", "2021-11-15 21:46"));
        list.add(new Note("note4", "note test4", "2021-11-20 11:01"));
        return list;
    }
}
