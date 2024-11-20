package com.example.banmaybay2;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GameGLRenderer implements GLSurfaceView.Renderer {
    private int shaderProgram;
    private Plane playerPlane;
    private List<Bullet> bullets = new ArrayList<>();
    private float planeX = 0.0f;
    private float planeY = 0.0f;
    private List<EnemyPlane> enemyPlanes = new ArrayList<>(); // List for enemy planes
    private long lastBulletTime = 0;

    public void setPlanePosition(float x, float y) {
        planeX = x;
        planeY = y;
    }
    public void shootBullet() {
        Bullet bullet = new Bullet(shaderProgram, planeX, planeY + 0.1f); // Create a bullet at the plane's position
        bullets.add(bullet);
    }

    private static final String VERTEX_SHADER_CODE =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    private static final String FRAGMENT_SHADER_CODE =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); // Màu nền
        shaderProgram = ShaderUtils.createProgram(VERTEX_SHADER_CODE, FRAGMENT_SHADER_CODE);

        playerPlane = new Plane(shaderProgram);  // Truyền shaderProgram vào Plane

        for (int i = 0; i < 5; i++) {  // Example: 5 enemy planes
            EnemyPlane enemyPlane = new EnemyPlane(shaderProgram, 3);
            enemyPlane.setPosition(0.1f * i, 0.8f);
            enemyPlanes.add(enemyPlane);
        }

    }


    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(shaderProgram);

        // Draw the player's plane
        playerPlane.setPosition(planeX, planeY);
        playerPlane.draw();

        // Check if it's time to shoot a bullet
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBulletTime >= 1000) { // 1 second interval
            bullets.add(new Bullet(shaderProgram, planeX, planeY));
            lastBulletTime = currentTime;
        }

        // Update and draw bullets
//        Iterator<Bullet> iterator = bullets.iterator();
//        while (iterator.hasNext()) {
//            Bullet bullet = iterator.next();
//            bullet.updatePosition();
////            if (bullet.isOffScreen()) {
//////                iterator.remove(); // Remove bullet if it's off-screen
////            } else {
////            }
//            bullet.draw();
//        }

        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            bullet.updatePosition();
            bullet.draw();
            if (bullet.isOffScreen()) {
                bullets.remove(i); // Remove bullet if it's off-screen
            } else {
                for (EnemyPlane enemyPlane : enemyPlanes) {
                    if (bullet.collidesWith(enemyPlane)) {
                        bullets.remove(i);
                        enemyPlane.decreaseHealth();
                    }
                }

                bullet.draw();
            }

        }

        for (int i = 0; i < enemyPlanes.size(); i++) {
            EnemyPlane enemyPlane = enemyPlanes.get(i);
            if (enemyPlane.getHealth() < 0) {
                enemyPlanes.remove(enemyPlane);
            } else {
                enemyPlane.draw();
            }
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

}
