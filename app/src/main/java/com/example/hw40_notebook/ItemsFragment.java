package com.example.hw40_notebook;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class ItemsFragment extends Fragment implements Serializable, IToast, IBasicDialog, ILog, IConst {

    private ListView lvItems;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    private Cursor cursor;
    private SimpleCursorAdapter itemsAdapter;
    private MenuItem miFilter;

    private IChangeFragment iChangeFragment;
    private IGit iGit;

    public ItemsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_items, container, false);

        initViews(view);

        if(getArguments() != null) {
            readArguments();

            initDatabase();
            initCursor();
            initAdapter();

            lvItems.setAdapter(itemsAdapter);
            initLvListener();
        }

        setHasOptionsMenu(true);
//        changeFilterIcon();
        return view;
    }

    private void changeFilterIcon() {
        if (databaseHelper.isFiltered()) {
            miFilter.setIcon(R.drawable.ic_baseline_filter_list_64_green);
        } else {
            miFilter.setIcon(R.drawable.ic_baseline_filter_list_64_white);
        }
    }

    private void readArguments() {
        databaseHelper = (DatabaseHelper) getArguments().getSerializable(KEY_DATABASE_HELPER);
    }

    private void initLvListener() {
        lvItems.setOnItemClickListener(this::clickItem);
    }

    private void clickItem(AdapterView<?> adapterView, View view, int i, long l) {
        cursor.moveToPosition(i);
        long id = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.ITEM_ID));
        iChangeFragment.showNoteFragment(id);
    }


    private void initAdapter() {
        String[] headers = new String[]{DatabaseHelper.ITEM_TITLE, DatabaseHelper.ITEM_DATE_TIME};
        itemsAdapter = new SimpleCursorAdapter(getContext(),
                R.layout.custom_two_line_list_item,
                cursor,
                headers,
                new int[]{android.R.id.text1, android.R.id.text2},
                0);
    }

    private void initCursor() {
        cursor = databaseHelper.getCursorItems(database);
    }

    private void initDatabase() {
        databaseHelper = (DatabaseHelper) getArguments().getSerializable(KEY_DATABASE_HELPER);
        database = databaseHelper.getReadableDatabase();
    }

    private void initViews(View view) {
        lvItems = view.findViewById(R.id.lvItems);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_items_fragment, menu);
        miFilter = menu.getItem(2);
        changeFilterIcon();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_back) {
            back();
        } else if(id == R.id.menu_add_note) {
            addNote();
        } else if(id == R.id.menu_sort) {
            sort();
        } else if(id == R.id.menu_filter) {
            filter();
        } else if(id == R.id.menu_git) {
            iGit.gitOpen();
        }
        return super.onOptionsItemSelected(item);
    }

    private void filter() {
        iChangeFragment.showFilterFragment();
    }

    private void sort() {
        iChangeFragment.showSortFragment();
    }

    private void addNote() {
        iChangeFragment.showNoteFragment(NoteFragment.CODE_NEW_ITEM);
    }

    private void back() {
        showEndDialog();
    }

    private void showEndDialog() {
        String message = "Do you want to exit the program?";
        String title = "Exit";
        showBasicDialog(getContext(), title, message, (v, d) -> getActivity().finish());
    }

}