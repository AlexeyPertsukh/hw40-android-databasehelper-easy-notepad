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

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class FilterFragment extends Fragment implements Serializable, IConst, IToast {
    private RadioButton rbFilterNone;
    private RadioButton rbFilterMonth;
    private RadioButton rbFilterWeek;
    private RadioButton rbFilterToday;

    private Map<RadioButton, DatabaseHelper.FILTER> mapRbFilter;
    private Map<DatabaseHelper.FILTER, RadioButton> mapIntFilter;

    private DatabaseHelper databaseHelper;

    private IChangeFragment iChangeFragment;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        iChangeFragment = (IChangeFragment) context;
    }

    public FilterFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapRbFilter = new HashMap<>();
        mapIntFilter = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        initViews(view);
        initMaps();
        if(getArguments() != null) {
            readArguments();
            setRbCheck();
        }
        initListeners();
        return view;
    }

    private void readArguments() {
        databaseHelper = (DatabaseHelper) getArguments().getSerializable(KEY_DATABASE_HELPER);
    }

    private void initMaps() {
        mapRbFilter.clear();
        mapRbFilter.put(rbFilterNone, DatabaseHelper.FILTER.NONE);
        mapRbFilter.put(rbFilterMonth, DatabaseHelper.FILTER.MONTH);
        mapRbFilter.put(rbFilterWeek, DatabaseHelper.FILTER.WEEK);
        mapRbFilter.put(rbFilterToday, DatabaseHelper.FILTER.TODAY);

        mapIntFilter.clear();
        mapIntFilter.put(DatabaseHelper.FILTER.NONE, rbFilterNone);
        mapIntFilter.put(DatabaseHelper.FILTER.MONTH, rbFilterMonth);
        mapIntFilter.put(DatabaseHelper.FILTER.WEEK, rbFilterWeek);
        mapIntFilter.put(DatabaseHelper.FILTER.TODAY, rbFilterToday);
    }

    private void initListeners() {
        rbFilterNone.setOnCheckedChangeListener(this::onRbCheck);
        rbFilterMonth.setOnCheckedChangeListener(this::onRbCheck);
        rbFilterWeek.setOnCheckedChangeListener(this::onRbCheck);
        rbFilterToday.setOnCheckedChangeListener(this::onRbCheck);
    }

    private void onRbCheck(CompoundButton compoundButton, boolean b) {
        if (!b) {
            return;
        }
        DatabaseHelper.FILTER filter = mapRbFilter.get(compoundButton);
        databaseHelper.setFilter(filter);
        saveFilterToShared(filter);

        String message = "Filter " + compoundButton.getText().toString().toLowerCase(Locale.ROOT);
        shortToast(getContext(), message);

        iChangeFragment.showItemsFragment();
    }


    private void initViews(View view) {
        rbFilterNone = view.findViewById(R.id.rbFilterNone);
        rbFilterMonth = view.findViewById(R.id.rbFilterMonth);
        rbFilterWeek = view.findViewById(R.id.rbFilterWeek);
        rbFilterToday = view.findViewById(R.id.rbFilterToday);
    }

    private void setRbCheck() {
        DatabaseHelper.FILTER filter = databaseHelper.getFilter();
        RadioButton rb = mapIntFilter.get(filter);
        assert rb != null;
        rb.setChecked(true);
    }

    private void saveFilterToShared(DatabaseHelper.FILTER filter) {
        MySharedPreferences mySharedPreferences = MySharedPreferences.getInstance(getContext());
        mySharedPreferences.putString(SHARED_FILTER_NAME, filter.name());
    }
}