package com.example.hw40_notebook;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class NoteFragment extends Fragment implements Serializable, IToast, IBasicDialog, ILog, IConst {
    
    private EditText etNoteName;
    private EditText etNoteMemo;
    private TextView tvDateTime;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    private Cursor cursor;

    private IChangeFragment iChangeFragment;
    private IGit iGit;

    private long noteId;

    public NoteFragment() {
    }


    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        iChangeFragment = (IChangeFragment) context;
        iGit = (IGit) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_note, container, false);
        initView(view);
        initListeners();

        if(getArguments() != null) {
            noteId = (Long) getArguments().getLong(KEY_NOTE_ID);
            initDatabase();
            initCursor();
            addToEditText();
        }

        setHasOptionsMenu(true);
        return view;
    }

    private void initCursor() {
        if(noteId < 0) {
            return;
        }
        cursor = databaseHelper.getCursorNoteById(database, noteId);
    }

    private void initDatabase() {
        databaseHelper = (DatabaseHelper) getArguments().getSerializable(KEY_DATABASE_HELPER);
        database = databaseHelper.getWritableDatabase();
    }

    private void initView(View view) {
        etNoteName = view.findViewById(R.id.etNoteName);
        etNoteMemo = view.findViewById(R.id.etNoteMemo);
        tvDateTime = view.findViewById(R.id.tvNoteDateTime);
    }

    private void initListeners() {
    }

    private void addToEditText() {
        if(noteId < 0) {
            clearTexts();
            return;
        }
        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ITEM_TITLE));
        etNoteName.setText(name);

        String memo = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ITEM_MEMO));
        etNoteMemo.setText(memo);

        String dt = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ITEM_DATE_TIME));
        tvDateTime.setText(dt);

    }

    private void clearTexts() {
        etNoteName.setText("");
        etNoteMemo.setText("");
        tvDateTime.setText("");
    }

    public void saveNote() {
        String name = etNoteName.getText().toString();
        String memo = etNoteMemo.getText().toString();

        if(noteId > 0) {
            databaseHelper.updateItemById(database, noteId, name, memo);
        } else {
            databaseHelper.insertItem(database, name, memo);
        }
        iChangeFragment.showItemsFragment();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    public void showConfirmDeleteDialog() {
        String message = "Do you want to delete the note?";
        String title = "Delete";
        showBasicDialog(getContext(), title, message, this::deleteNote);
    }

    private void deleteNote(DialogInterface dialogInterface, int i) {
        if(noteId < 1) {
            return;
        }
        databaseHelper.deleteItemById(database, noteId);
        shortToast(getContext(), "Note deleted");
        iChangeFragment.showItemsFragment();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_note_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_back) {
            back();
        } else if(id == R.id.menu_save_note) {
            saveNote();
        } else if(id == R.id.menu_delete_note) {
            showConfirmDeleteDialog();
        } else if(id == R.id.menu_git) {
            iGit.gitOpen();
        }
        return super.onOptionsItemSelected(item);
    }

    private void back() {
        iChangeFragment.showItemsFragment();
    }


}