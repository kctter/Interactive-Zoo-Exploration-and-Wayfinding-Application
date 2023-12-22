package com.bennettzhang.zooseeker;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    AddExhibitFragment addExhibitFragment;
    PlanFragment planFragment;
    DirectionsFragment directionsFragment;
    SwitchCompat directionToggle;
    LocationPermissionChecker locationPermissionChecker;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        fragmentManager = getSupportFragmentManager();
        addExhibitFragment = new AddExhibitFragment();
        planFragment = new PlanFragment();
        directionsFragment = new DirectionsFragment();
        directionToggle = findViewById(R.id.DirectionToggle);

        directionToggle.setOnClickListener((View view) -> {
            DirectionsFragment.brief = directionToggle.isChecked();
            directionsFragment.setDirections();
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.add_exhibit) {
                fragmentManager.beginTransaction()
                        .replace(R.id.container, addExhibitFragment)
                        .commit();
                setTitle(R.string.add_exhibit_title);
                directionToggle.setVisibility(View.GONE);
                return true;
            } else if (id == R.id.plan) {
                fragmentManager.beginTransaction()
                        .replace(R.id.container, planFragment)
                        .commit();
                setTitle(R.string.summary_title);
                directionToggle.setVisibility(View.GONE);
                return true;
            } else if (id == R.id.directions) {
                fragmentManager.beginTransaction().
                        replace(R.id.container, directionsFragment)
                        .commit();
                setTitle(R.string.directions_title);
                directionToggle.setVisibility(View.VISIBLE);
                return true;
            }

            return false;
        });

        bottomNavigationView.setSelectedItemId(R.id.add_exhibit);
        LocationPermissionChecker permissionChecker = new LocationPermissionChecker(this);
        permissionChecker.ensurePermissions();
    }
}
