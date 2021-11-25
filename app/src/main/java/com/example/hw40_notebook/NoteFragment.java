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

import com.example.constants.IConst;
import com.example.model.DatabaseHelper;
import com.example.util.IBasicDialog;
import com.example.util.ILog;
import com.example.util.IToast;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class NoteFragment extends Fragment implements Serializable, IToast, IBasicDialog, ILog, IConst {

    private static final String KEY_TITLE_SELECT_START = "key_title_select_start";
    private static final String KEY_TITLE_SELECT_END = "key_title_select_end";
    private static final String KEY_MEMO_SELECT_START = "key_memo_select_start";
    private static final String KEY_MEMO_SELECT_END = "key_memo_select_end";

    private EditText etNoteTitle;
    private EditText etNoteMemo;
    private TextView tvDateTime;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

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
        printLog("NoteFragment - Create: " + toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_note, container, false);
        printLog("NoteFragment - CreateView");

        initView(view);
        initListeners();

        if(savedInstanceState != null) {
            loadFromSaveInstance(savedInstanceState);
        } else if(getArguments() != null) {
            loadFromArguments();
        }

        setHasOptionsMenu(true);

        return view;
    }

    private void loadFromArguments() {
        assert getArguments() != null;
        noteId = getArguments().getLong(KEY_NOTE_ID);
        databaseHelper = (DatabaseHelper) getArguments().getSerializable(KEY_DATABASE_HELPER);
        assert databaseHelper != null;
        database = databaseHelper.getWritableDatabase();
        Cursor cursor = databaseHelper.getCursorNoteById(database, noteId);
        addToEditText(cursor);
    }

    private void loadFromSaveInstance(Bundle bundle) {
        printLog("NoteFragment - read savedInstanceState: title = " + bundle.getString("title"));
        databaseHelper = (DatabaseHelper) bundle.getSerializable(KEY_DATABASE_HELPER);
        assert databaseHelper != null;
        database = databaseHelper.getWritableDatabase();

        noteId = bundle.getLong(DatabaseHelper.ITEM_ID);
        String title = bundle.getString(DatabaseHelper.ITEM_TITLE);
        String memo =  bundle.getString(DatabaseHelper.ITEM_MEMO);
        String dt  =  bundle.getString(DatabaseHelper.ITEM_DATE_TIME);

        addToEditText(title, memo, dt);
        setEtSelection(bundle);
    }

    private void setEtSelection(Bundle bundle) {
        int titleSelectStart = bundle.getInt(KEY_TITLE_SELECT_START);
        int titleSelectEnd = bundle.getInt(KEY_TITLE_SELECT_END);
        int memoSelectStart = bundle.getInt(KEY_MEMO_SELECT_START);
        int memoSelectEnd = bundle.getInt(KEY_MEMO_SELECT_END);

        etNoteTitle.setSelection(titleSelectStart, titleSelectEnd);
        etNoteMemo.setSelection(memoSelectStart, memoSelectEnd);
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(DatabaseHelper.ITEM_TITLE, etNoteTitle.getText().toString());
        outState.putString(DatabaseHelper.ITEM_MEMO, etNoteMemo.getText().toString());
        outState.putString(DatabaseHelper.ITEM_DATE_TIME, tvDateTime.getText().toString());
        outState.putLong(DatabaseHelper.ITEM_ID, noteId);
        outState.putSerializable(KEY_DATABASE_HELPER, databaseHelper);
        outState.putInt(KEY_TITLE_SELECT_START, etNoteTitle.getSelectionStart());
        outState.putInt(KEY_TITLE_SELECT_END, etNoteTitle.getSelectionEnd());
        outState.putInt(KEY_MEMO_SELECT_START, etNoteMemo.getSelectionStart());
        outState.putInt(KEY_MEMO_SELECT_END, etNoteMemo.getSelectionEnd());

        printLog("noteFragment - onSaveInstanceState");
    }

    private void initView(View view) {
        etNoteTitle = view.findViewById(R.id.etNoteTitle);
        etNoteMemo = view.findViewById(R.id.etNoteMemo);
        tvDateTime = view.findViewById(R.id.tvNoteDateTime);
    }

    private void initListeners() {
    }

    private void addToEditText(Cursor cursor) {
        if(isNewNote()) {
            clearTexts();
            return;
        }
        cursor.moveToFirst();
        String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ITEM_TITLE));
        String memo = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ITEM_MEMO));
        String dt = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ITEM_DATE_TIME));
        addToEditText(title, memo, dt);
    }

    private void addToEditText(String title, String memo, String dt) {
        etNoteTitle.setText(title);
        etNoteMemo.setText(memo);
        tvDateTime.setText(dt);
    }

    private void clearTexts() {
        etNoteTitle.setText("");
        etNoteMemo.setText("");
        tvDateTime.setText("");
    }

    public void saveNote() {
        String name = etNoteTitle.getText().toString();
        String memo = etNoteMemo.getText().toString();

        if(isEditNote()) {
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
        if(isEditNote()) {
            databaseHelper.deleteItemById(database, noteId);
        }
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

    private boolean isNewNote() {
        return noteId < 1;
    }

    private boolean isEditNote() {
        return !isNewNote();
    }

}