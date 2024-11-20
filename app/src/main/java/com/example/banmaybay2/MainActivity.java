package com.example.banmaybay2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.banmaybay2.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initBottomNavigation();
    }

    private void initBottomNavigation() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, StoreFragment.newInstance()).commit();
        binding.bnMain.setSelectedItemId(R.id.nav_play);
        binding.bnMain.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                if (R.id.nav_upgrade == id) {
                    fragmentTransaction
                            .replace(R.id.fragment_container, StoreFragment.newInstance())
                            .commit();
                }
                if (R.id.nav_store == id) {
                    fragmentTransaction
                            .replace(R.id.fragment_container, UpgradeFragment.newInstance())
                            .commit();
                }
                if (R.id.nav_play == id) {
                }

                return true;
            }

        });

        binding.fabPlay.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PlayActivity.class);
            startActivity(intent);
        });
    }

}