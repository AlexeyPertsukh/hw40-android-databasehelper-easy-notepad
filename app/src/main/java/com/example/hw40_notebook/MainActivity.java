package com.example.hw40_notebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.constants.IConst;
import com.example.model.DatabaseHelper;
import com.example.model.MySharedPreferences;
import com.example.util.IBasicDialog;
import com.example.util.ILog;
import com.example.util.IToast;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements IChangeFragment, IConst, IToast, ILog, IBasicDialog, IGit {

    private DatabaseHelper databaseHelper;

    private ItemsFragment itemsFragment;
    private NoteFragment noteFragment;
    private FilterFragment filterFragment;
    private SortFragment sortFragment;
    private FragmentContainerView fcMain;

    MySharedPreferences mySharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean recovery = false;
        changeStatusBarColor();
        if (savedInstanceState != null) {
            printLog("MainActivity - read savedInstanceState");
            recovery = true;
        }
        initViews();
        initSharedPreferences();
        initDatabaseHelper();
        initFragments();

        if (!recovery) {
            showItemsFragment();
        }

        printLog("MainActivity - Create");
    }

    private void changeStatusBarColor() {
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));
    }

    private void initViews() {
        fcMain = findViewById(R.id.fcMain);
    }

    private void initSharedPreferences() {
        mySharedPreferences = MySharedPreferences.getInstance(getApplicationContext());
    }

    private void initDatabaseHelper() {
        DatabaseHelper.SORT sort = readSortFromShared();
        DatabaseHelper.FILTER filter = readFilterFromShared();
        databaseHelper = DatabaseHelper.of(this, sort, filter);
    }

    private void initFragments() {
        itemsFragment = new ItemsFragment();
        noteFragment = new NoteFragment();
        filterFragment = new FilterFragment();
        sortFragment = new SortFragment();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("message", "recovery");
        printLog("MainActivity - onSaveInstanceState");
    }

    @Override
    public void
    showItemsFragment() {
        Bundle args = new Bundle();
        args.putSerializable(KEY_DATABASE_HELPER, databaseHelper);
        itemsFragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fcMain, itemsFragment)
                .commit();
    }

    @Override
    public void showNoteFragment(long itemId) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_DATABASE_HELPER, databaseHelper);
        args.putLong(KEY_NOTE_ID, itemId);
        noteFragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fcMain, noteFragment)
                .commit();

    }

    @Override
    public void showFilterFragment() {
        Bundle args = new Bundle();
        args.putSerializable(KEY_DATABASE_HELPER, databaseHelper);
        filterFragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fcMain, filterFragment)
                .commit();
    }

    @Override
    public void showSortFragment() {
        Bundle args = new Bundle();
        args.putSerializable(KEY_DATABASE_HELPER, databaseHelper);
        sortFragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fcMain, sortFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_back) {
            back();
        } else if (id == R.id.menu_git) {
            gitOpen();
        }
        return super.onOptionsItemSelected(item);
    }


    private void back() {
        showItemsFragment();
    }

    private void showEndDialog() {
        String message = "Do you want to exit the program?";
        String title = "Exit";
        showBasicDialog(this, title, message, (v, d) -> finish());
    }

    @Override
    public void onBackPressed() {
        if (itemsFragment.isVisible()) {
            showEndDialog();
        } else {
            showItemsFragment();
        }
    }

    @Override
    public void gitOpen() {
        Intent Browse = new Intent(Intent.ACTION_VIEW, Uri.parse(GIT_URL));
        startActivity(Browse);
    }

    private DatabaseHelper.SORT readSortFromShared() {
        String name = mySharedPreferences.getString(SHARED_SORT_NAME, DatabaseHelper.DEFAULT_SORT.name());
        return DatabaseHelper.SORT.valueOf(name);
    }

    private DatabaseHelper.FILTER readFilterFromShared() {
        String name = mySharedPreferences.getString(SHARED_FILTER_NAME, DatabaseHelper.DEFAULT_FILTER.name());
        return DatabaseHelper.FILTER.valueOf(name);
    }

}