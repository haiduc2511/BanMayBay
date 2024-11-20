package com.example.banmaybay2;

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

//    private void initBottomNavigation() {
//        mainFragment = MainFragment.newInstance();
//        imageUploadListener = mainFragment;
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_main, mainFragment).commit();
//        binding.bnMain.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                int id = menuItem.getItemId();
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                if (R.id.nav_upgrade == id) {
//                    fragmentTransaction
//                            .replace(R.id.fragment_container, ManageCategoryFragment.newInstance())
//                            .commit();
//                }
//                if (R.id.nav_store == id) {
//                    fragmentTransaction
//                            .replace(R.id.fragment_container, AccountFragment.newInstance())
//                            .commit();
//                }
//                if (R.id.nav_play == id) {
//                    fragmentTransaction
//                            .replace(R.id.fragment_container, AccountFragment.newInstance())
//                            .commit();
//                }
//
//                return true;
//            }
//
//        });
//    }

}