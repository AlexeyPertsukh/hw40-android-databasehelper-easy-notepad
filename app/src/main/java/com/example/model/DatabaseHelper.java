package com.example.model;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.util.ILog;

import java.io.Serializable;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper implements Serializable, ILog {

    public static final String DATABASE_NAME = "notepad.db"; // название бд
    public static final String TAB_ITEMS = "items";
    public static final String ITEM_ID = "_id";
    public static final String ITEM_TITLE = "title";
    public static final String ITEM_DATE_TIME = "dt";
    public static final String ITEM_MEMO = "memo";
    private static final int VERSION = 21;

    public static SORT DEFAULT_SORT = SORT.EDIT_OLD;
    public static FILTER DEFAULT_FILTER = FILTER.NONE;

    private SORT sort;
    private FILTER filter;

    private DatabaseHelper(Context context, SORT sort, FILTER filter) {
        super(context, DATABASE_NAME, null, VERSION);
        this.sort = sort;
        this.filter = filter;
    }

    public static DatabaseHelper of(Context context, SORT sort, FILTER filter) {
        return new DatabaseHelper(context, sort, filter);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT);", TAB_ITEMS,
                ITEM_ID,
                ITEM_TITLE,
                ITEM_DATE_TIME,
                ITEM_MEMO);
        db.execSQL(query);

        List<Note> notes = DefaultItems.getNotes();
        insertItemsFromList(db, notes);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + TAB_ITEMS;
        db.execSQL(query);
        onCreate(db);
    }

    public int updateItemById(SQLiteDatabase db, long itemId, Note note) {
        ContentValues cv = new ContentValues();
        cv.put(ITEM_TITLE, note.title);
        cv.put(ITEM_MEMO, note.memo);
        cv.put(ITEM_DATE_TIME, note.dt);
        return db.update(DatabaseHelper.TAB_ITEMS, cv
                , DatabaseHelper.ITEM_ID + "=?"
                , new String[]{String.valueOf(itemId)});
    }


    public void insertItem(SQLiteDatabase db, Note note) {
        String query = String.format("INSERT INTO items(%s, %s, %s) VALUES('%s','%s','%s')",
                ITEM_TITLE, ITEM_MEMO, ITEM_DATE_TIME,
                note.title, note.memo, note.dt);

        db.execSQL(query);
    }


    public void insertItemsFromList(SQLiteDatabase db, List<Note> notes) {
        for (Note note : notes) {
            insertItem(db, note);
        }
    }


    public void deleteItemById(SQLiteDatabase db, long itemId) {
        @SuppressLint("DefaultLocale") String query = String.format("DELETE FROM %s WHERE %s=%d", TAB_ITEMS, ITEM_ID, itemId);
        db.execSQL(query);
    }



    public Cursor getCursorItems(SQLiteDatabase db) {
        String query = String.format("SELECT * FROM %s %s %s", DatabaseHelper.TAB_ITEMS,
                filter.getQuery(),
                sort.getQuery());
        printLog(query);    //LOG
        return db.rawQuery(query, null);
    }

    public Cursor getCursorNoteById(SQLiteDatabase db, long id) {
        @SuppressLint("DefaultLocale") String query = String.format("select * from %s where %s = %d", TAB_ITEMS, ITEM_ID, id);
        return db.rawQuery(query, null);
    }

    public enum FILTER implements Serializable {
        NONE(""),
        MONTH("WHERE " + ITEM_DATE_TIME + " > date('now','-1 month')"),
        WEEK("WHERE " + ITEM_DATE_TIME + " > date('now','-7 day')"),
        TODAY("WHERE date(" + ITEM_DATE_TIME + ") = date('now')"),
        ;

        private final String query;

        FILTER(String query) {
            this.query = query;
        }

        public String getQuery() {
            return query;
        }
    }

    public enum SORT  implements Serializable {
        EDIT_OLD("ORDER BY " + ITEM_DATE_TIME + " ASC"),
        EDIT_NEW("ORDER BY " + ITEM_DATE_TIME + " DESC"),
        ORDER_OLD("ORDER BY " + ITEM_ID + " ASC"),
        ORDER_NEW("ORDER BY " + ITEM_ID + " DESC"),
        TITLE_LO("ORDER BY " + ITEM_TITLE + " ASC"),
        TITLE_HI("ORDER BY " + ITEM_TITLE + " DESC");

        private final String query;

        SORT(String query) {
            this.query = query;
        }

        public String getQuery() {
            return query;
        }
    }

    public SORT getSort() {
        return sort;
    }

    public FILTER getFilter() {
        return filter;
    }

    public void setSort(SORT sort) {
        this.sort = sort;
    }

    public void setFilter(FILTER filter) {
        this.filter = filter;
    }

    public boolean isFiltered() {
        return filter != FILTER.NONE;
    }

}