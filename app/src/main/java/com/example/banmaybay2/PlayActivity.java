package com.example.banmaybay2;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PlayActivity extends AppCompatActivity {

    private GameGLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Tạo một GameGLSurfaceView và sử dụng nó như nội dung chính
        glSurfaceView = new GameGLSurfaceView(this);
        setContentView(glSurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();  // Tạm dừng render khi activity bị tạm dừng
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();  // Tiếp tục render khi activity trở lại
    }
}