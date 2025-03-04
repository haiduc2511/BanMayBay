package com.example.banmaybay2;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.widget.Button;
import android.widget.Toast;

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
    private Context context;
    SharedPrefManager sharedPrefManager;
    int bulletPerShot = 1;
    int enemyPlaneHealth = 1;
    int bulletSpeed = 1;
    int enemyPlaneSpeed = 1;
    int level = 1;

    public GameGLRenderer(Context context, int level) {
        this.context = context;
        this.level = level;
        sharedPrefManager = new SharedPrefManager(context);
        bulletPerShot = Integer.parseInt(sharedPrefManager.getBulletsPerShot());
        enemyPlaneHealth = Integer.parseInt(sharedPrefManager.getMyPlaneHealth());
        bulletSpeed = Integer.parseInt(sharedPrefManager.getBulletSpeed());
        enemyPlaneSpeed = Integer.parseInt(sharedPrefManager.getEnemyPlaneSpeed());
    }


    public void setPlanePosition(float x, float y) {
        planeX = x;
        planeY = y;
    }
    public void shootBullet() {
        Bullet bullet = new Bullet(shaderProgram, planeX, planeY + 0.1f * bulletSpeed); // Create a bullet at the plane's position
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

        for (int i = 0; i < 9; i++) {  // Example: 5 enemy planes
            for (int j = 0; j < 10; j++) {
                EnemyPlane enemyPlane = new EnemyPlane(shaderProgram, 3 * level);
                enemyPlane.setPosition(-0.9f + 0.2f * j, 2 + 0.5f * i);
                enemyPlanes.add(enemyPlane);
            }
        }

    }


    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(shaderProgram);

        playerPlane.setPosition(planeX, planeY);
        playerPlane.draw();

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBulletTime >= 100) { // 1 second interval
            for (int i = 0; i < bulletPerShot; i++) {
                bullets.add(new Bullet(shaderProgram
                        , planeX - 0.05f * (bulletPerShot / 2) + 0.05f * i, planeY));
                lastBulletTime = currentTime;
            }
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
            bullet.updatePosition(bulletSpeed);
            bullet.draw();
            if (bullet.isOffScreen()) {
                bullets.remove(i); // Remove bullet if it's off-screen
            } else {
                for (int j = 0; j < enemyPlanes.size(); j++) {
                    EnemyPlane enemyPlane = enemyPlanes.get(j);
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
            enemyPlane.updatePosition();
            if (playerPlane.collidesWith(enemyPlane)) {
                ((Activity) context).runOnUiThread(() -> {
                    showGameOverFragment();
                    Toast.makeText(context, "Thua roi", Toast.LENGTH_SHORT).show();
                });
            }
            if (enemyPlane.getPosY() < -1.0f) {
                enemyPlanes.remove(enemyPlane);
            }
            if (enemyPlane.getHealth() < 0) {
                enemyPlanes.remove(enemyPlane);
            } else {
                enemyPlane.draw();
            }
        }

        if (enemyPlanes.size() == 0) {
            ((Activity) context).runOnUiThread(() -> {
                Toast.makeText(context, "Thang roi", Toast.LENGTH_SHORT).show();
            });

        }

    }
    private void showGameOverFragment() {
//        ((Activity) context).runOnUiThread(() -> {
//            if (context instanceof PlayActivity) {
//                ((PlayActivity) context).showGameOverFragment();
//            }
//        });
    }


    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

}
