package com.example.banmaybay2;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Plane {
    private FloatBuffer vertexBuffer;
    private int shaderProgram;

    private static final int COORDS_PER_VERTEX = 3;
    private final int vertexCount = 3;  // Triangular plane with three vertices
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    private float[] color = {0.6f, 0.7f, 0.2f, 1.0f};
    private float[] vertexCoords;

    public Plane(int shaderProgram) {
        this.shaderProgram = shaderProgram;
        vertexCoords = new float[]{
                0.0f,  0.1f, 0.0f,   // top
                -0.1f, -0.1f, 0.0f,  // bottom left
                0.1f, -0.1f, 0.0f    // bottom right
        };
        setupVertexBuffer();
    }

    private void setupVertexBuffer() {
        ByteBuffer bb = ByteBuffer.allocateDirect(vertexCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertexCoords);
        vertexBuffer.position(0);
    }

    public void setPosition(float x, float y) {
        // Adjust coordinates based on the new position
        vertexCoords[0] = x;      // Top vertex X
        vertexCoords[1] = y + 0.1f; // Top vertex Y
        vertexCoords[3] = x - 0.1f; // Bottom left X
        vertexCoords[4] = y - 0.1f; // Bottom left Y
        vertexCoords[6] = x + 0.1f; // Bottom right X
        vertexCoords[7] = y - 0.1f; // Bottom right Y

        setupVertexBuffer(); // Update buffer with new coordinates
    }

    public void draw() {
        int positionHandle = GLES20.glGetAttribLocation(shaderProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

        int colorHandle = GLES20.glGetUniformLocation(shaderProgram, "vColor");
        GLES20.glUniform4fv(colorHandle, 1, color, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
