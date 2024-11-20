package com.example.banmaybay2;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Bullet {
    private FloatBuffer vertexBuffer;
    private int shaderProgram;
    private float bulletX;
    private float bulletY;
    private static final float COLLISION_RADIUS = 0.1f;

    private static final float[] VERTEX_COORDS = {
            0.0f,  0.05f, 0.0f,
            -0.02f, -0.05f, 0.0f,
            0.02f, -0.05f, 0.0f
    };
    private static final int COORDS_PER_VERTEX = 3;
    private final int vertexCount = VERTEX_COORDS.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4;

    private float[] color = {0.0f, 1.0f, 0.0f, 1.0f};

    public Bullet(int shaderProgram, float startX, float startY) {
        this.shaderProgram = shaderProgram;
        bulletX = startX;
        bulletY = startY;

        ByteBuffer bb = ByteBuffer.allocateDirect(VERTEX_COORDS.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(VERTEX_COORDS);
        vertexBuffer.position(0);
    }

    public void updatePosition() {
        bulletY += 0.01f;
    }

    public boolean isOffScreen() {
        return bulletY > 1.0f;
    }

    public boolean collidesWith(EnemyPlane enemyPlane) {
        float dx = bulletX - enemyPlane.getPosX();
        float dy = bulletY - enemyPlane.getPosY();
        float distanceSquared = dx * dx + dy * dy;
        return distanceSquared < COLLISION_RADIUS * COLLISION_RADIUS;
    }

    public void draw() {
        int positionHandle = GLES20.glGetAttribLocation(shaderProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);

        float[] transformedCoords = {
                bulletX, bulletY + 0.05f, 0.0f,
                bulletX - 0.02f, bulletY - 0.05f, 0.0f,
                bulletX + 0.02f, bulletY - 0.05f, 0.0f
        };
        vertexBuffer.put(transformedCoords).position(0);

        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

        int colorHandle = GLES20.glGetUniformLocation(shaderProgram, "vColor");
        GLES20.glUniform4fv(colorHandle, 1, color, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
