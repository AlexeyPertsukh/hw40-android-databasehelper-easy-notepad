package com.example.hw40_notebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements IChangeFragment, IConst, IToast, IBasicDialog, IGit {

    private DatabaseHelper databaseHelper;

    private ItemsFragment itemsFragment;
    private NoteFragment noteFragment;
    private FilterFragment filterFragment;
    private SortFragment sortFragment;
    private FragmentContainerView fcMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initDatabase();
        initFragments();
        showItemsFragment();
    }

    private void initViews() {
        fcMain = findViewById(R.id.fcMain);
    }

    private void initDatabase() {
        databaseHelper = new DatabaseHelper(this);
    }

    private void initFragments() {
        itemsFragment = new ItemsFragment();
        noteFragment = new NoteFragment();
        filterFragment = new FilterFragment();
        sortFragment = new SortFragment();
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
        if(id == R.id.menu_back) {
            back();
        } else if(id == R.id.menu_git) {
            openGit();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openGit() {
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void gitOpen() {

    }
}