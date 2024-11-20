package com.example.banmaybay2;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.View;

public class GameGLSurfaceView extends GLSurfaceView {
    private final GameGLRenderer renderer;

    public GameGLSurfaceView(Context context) {
        super(context);

        // Thiết lập phiên bản OpenGL ES 2.0
        setEGLContextClientVersion(2);

        // Khởi tạo renderer và gán vào GLSurfaceView
        renderer = new GameGLRenderer();
        setRenderer(renderer);

        // Chế độ render khi có thay đổi (hoặc set to CONTINUOUSLY để vẽ liên tục)
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event != null) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        renderer.shootBullet();  // Trigger bullet shooting
                    }

                    float normalizedX = (event.getX() / (float) getWidth()) * 2 - 1;
                    float normalizedY = -((event.getY() / (float) getHeight()) * 2 - 1);
                    renderer.setPlanePosition(normalizedX, normalizedY);
                    return true;
                }
                return false;
            }
        });

    }
}
