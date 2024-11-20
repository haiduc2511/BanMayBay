package com.example.banmaybay2;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class EnemyPlane {
    private FloatBuffer vertexBuffer;
    private int shaderProgram;
    private int health = 3;
    private float posX, posY;

    private static final float[] VERTEX_COORDS = {
            0.0f,  -0.1f, 0.0f,
            0.1f, 0.1f, 0.0f,
            -0.1f, 0.1f, 0.0f
    };
    private static final int COORDS_PER_VERTEX = 3;
    private final int vertexCount = VERTEX_COORDS.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4;

    private float[] color = {1.0f, 0.0f, 0.0f, 1.0f};

    public EnemyPlane(int shaderProgram, int health) {
        this.health = health;
        this.shaderProgram = shaderProgram;
        ByteBuffer bb = ByteBuffer.allocateDirect(VERTEX_COORDS.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(VERTEX_COORDS);
        vertexBuffer.position(0);
    }
    public void decreaseHealth() {
        this.health--;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setPosition(float x, float y) {
        posX = x;
        posY = y;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public void draw() {
        int positionHandle = GLES20.glGetAttribLocation(shaderProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);

        float[] transformedCoords = {
                posX, posY - 0.1f, 0.0f,
                posX + 0.1f, posY + 0.1f, 0.0f,
                posX - 0.1f, posY + 0.1f, 0.0f
        };
        vertexBuffer.put(transformedCoords).position(0);

        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

        int colorHandle = GLES20.glGetUniformLocation(shaderProgram, "vColor");
        GLES20.glUniform4fv(colorHandle, 1, color, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}