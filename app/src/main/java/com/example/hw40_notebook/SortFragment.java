package com.example.hw40_notebook;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.example.constants.IConst;
import com.example.model.DatabaseHelper;
import com.example.model.MySharedPreferences;
import com.example.util.ILog;
import com.example.util.IToast;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SortFragment extends Fragment implements Serializable, IConst, IToast, ILog {

    private RadioButton rbSortOrderNew;
    private RadioButton rbSortOrderOld;
    private RadioButton rbSortEditNew;
    private RadioButton rbSortEditOld;
    private RadioButton rbSortTitleHi;
    private RadioButton rbSortTitleLo;

    private Map<RadioButton, DatabaseHelper.SORT> mapRbSort;
    private Map<DatabaseHelper.SORT, RadioButton> mapSortRb;

    private DatabaseHelper databaseHelper;

    private IChangeFragment iChangeFragment;

    public SortFragment() {
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        iChangeFragment = (IChangeFragment) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapRbSort = new HashMap<>();
        mapSortRb = new HashMap<>();
        printLog("SortFragment - Create");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_sort, container, false);
        initViews(view);
        initMaps();
        if(getArguments() != null) {
            readArguments();
            setRbCheck();
        }
        initListeners();
        printLog("SortFragment - CreateView");
        return view;
    }

    private void setRbCheck() {
        DatabaseHelper.SORT sort = databaseHelper.getSort();
        RadioButton rb = mapSortRb.get(sort);
        assert rb != null;
        rb.setChecked(true);
    }

    private void readArguments() {
        databaseHelper = (DatabaseHelper) getArguments().getSerializable(KEY_DATABASE_HELPER);
    }

    private void initMaps() {
        mapRbSort.clear();
        mapRbSort.put(rbSortOrderNew, DatabaseHelper.SORT.ORDER_NEW);
        mapRbSort.put(rbSortOrderOld, DatabaseHelper.SORT.ORDER_OLD);
        mapRbSort.put(rbSortEditNew, DatabaseHelper.SORT.EDIT_NEW);
        mapRbSort.put(rbSortEditOld, DatabaseHelper.SORT.EDIT_OLD);
        mapRbSort.put(rbSortTitleHi, DatabaseHelper.SORT.TITLE_HI);
        mapRbSort.put(rbSortTitleLo, DatabaseHelper.SORT.TITLE_LO);

        mapSortRb.clear();
        mapSortRb.put(DatabaseHelper.SORT.ORDER_NEW, rbSortOrderNew);
        mapSortRb.put(DatabaseHelper.SORT.ORDER_OLD, rbSortOrderOld);
        mapSortRb.put(DatabaseHelper.SORT.EDIT_NEW, rbSortEditNew);
        mapSortRb.put(DatabaseHelper.SORT.EDIT_OLD, rbSortEditOld);
        mapSortRb.put(DatabaseHelper.SORT.TITLE_HI, rbSortTitleHi);
        mapSortRb.put(DatabaseHelper.SORT.TITLE_LO, rbSortTitleLo);
    }

    private void initListeners() {
        rbSortOrderNew.setOnCheckedChangeListener(this::onRbCheck);
        rbSortOrderOld.setOnCheckedChangeListener(this::onRbCheck);
        rbSortEditNew.setOnCheckedChangeListener(this::onRbCheck);
        rbSortEditOld.setOnCheckedChangeListener(this::onRbCheck);
        rbSortTitleHi.setOnCheckedChangeListener(this::onRbCheck);
        rbSortTitleLo.setOnCheckedChangeListener(this::onRbCheck);
    }

    private void onRbCheck(CompoundButton compoundButton, boolean b) {
        if (!b) {
            return;
        }
        DatabaseHelper.SORT sort = mapRbSort.get(compoundButton);
        databaseHelper.setSort(sort);
        saveSortToShared(sort);

        String message = "Sort by " + compoundButton.getText().toString().toLowerCase(Locale.ROOT);
        shortToast(getContext(), message);
        iChangeFragment.showItemsFragment();
    }

    private void initViews(View view) {
        rbSortOrderNew = view.findViewById(R.id.rbSortOrderNew);
        rbSortOrderOld = view.findViewById(R.id.rbSortOrderOld);
        rbSortEditNew = view.findViewById(R.id.rbSortEditNew);
        rbSortEditOld = view.findViewById(R.id.rbSortEditOld);
        rbSortTitleHi = view.findViewById(R.id.rbSortTitleHi);
        rbSortTitleLo = view.findViewById(R.id.rbSortTitleLo);
    }

    private void saveSortToShared(DatabaseHelper.SORT sort) {
        MySharedPreferences mySharedPreferences = MySharedPreferences.getInstance(getContext());
        mySharedPreferences.putString(SHARED_SORT_NAME, sort.name());
    }
}