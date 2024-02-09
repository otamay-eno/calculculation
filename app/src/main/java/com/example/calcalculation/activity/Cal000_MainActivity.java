package com.example.calcalculation.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.example.calcalculation.R;
import com.example.calcalculation.base.CalBaseActivity;
import com.example.calcalculation.fragment.Cal001_InputFragment;
import com.example.calcalculation.fragment.Cal005_SelectDateFragment;
import com.example.calcalculation.fragment.Cal008_BarcodeReportFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class Cal000_MainActivity extends CalBaseActivity {

    private BottomNavigationView bottomNavigationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        int fragmentIndex = intent.getIntExtra("fragment_index", 0);

        if(fragmentIndex == 0){
            bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.cal001_input_fragment:
                            selectedFragment = new Cal001_InputFragment();
                            break;
                        case R.id.cal005_select_date_fragment:
                            selectedFragment = new Cal005_SelectDateFragment();
                            break;
                        case R.id.cal008_barcodereport_fragment:
                            selectedFragment = new Cal008_BarcodeReportFragment();
                    }

                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, selectedFragment)
                                .commit();
                    }

                    return true;
                }
            });

            // 初期フラグメントを設定
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new Cal001_InputFragment())
                    .commit();

        }else if(fragmentIndex == 1){
            bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.cal001_input_fragment:
                            selectedFragment = new Cal001_InputFragment();
                            break;
                        case R.id.cal005_select_date_fragment:
                            selectedFragment = new Cal005_SelectDateFragment();
                            break;
                        case R.id.cal008_barcodereport_fragment:
                            selectedFragment = new Cal008_BarcodeReportFragment();
                    }

                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, selectedFragment)
                                .commit();
                    }

                    return true;
                }
            });

            // 初期フラグメントを設定
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new Cal008_BarcodeReportFragment())
                    .commit();
        }
    }
}