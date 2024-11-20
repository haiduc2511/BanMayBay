package com.example.banmaybay2;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Plane {
    private FloatBuffer vertexBuffer;
    private int shaderProgram;

    private static final int COORDS_PER_VERTEX = 3;
    private final int vertexStride = COORDS_PER_VERTEX * 4;

    private float[] vertexCoords; // Coordinates for the entire plane

    // Colors for each part of the plane
    private float[] fuselageColor = {0.3f, 0.7f, 0.3f, 1.0f}; // Green
    private float[] wingColor = {0.8f, 0.8f, 0.0f, 1.0f};     // Yellow
    private float[] tailColor = {1.0f, 0.0f, 0.0f, 1.0f};     // Red

    private float positionX = 0.0f, positionY = 0.0f;

    public Plane(int shaderProgram) {
        this.shaderProgram = shaderProgram;

        // Define the 2D plane geometry (fuselage, wings, tail)
        vertexCoords = new float[] {
                // Fuselage (center body)
                0.0f,  0.05f, 0.0f,  // Top of fuselage
                -0.1f, -0.2f, 0.0f,  // Bottom left of fuselage
                0.1f, -0.2f, 0.0f,  // Bottom right of fuselage

                // Left wing
                -0.06f, -0.18f, 0.0f,  // Top left wing
                -0.09f, -0.263f, 0.0f,  // Bottom left wing
                -0.03f,  -0.263f, 0.0f,  // Bottom center wing

                // Right wing
                0.052f, -0.184f, 0.0f,  // Top right wing
                0.0195f, -0.259f, 0.0f,  // Bottom right wing
                0.0846f, -0.2599f, 0.0f,  // Bottom center wing

                // Tail
                0.0f,  -0.15f, 0.0f,  // Center of tail
                -0.15f,  -0.05f, 0.0f, // Top left tail
                0.15f,  -0.05f, 0.0f  // Top right tail
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
        float dx = x - positionX;
        float dy = y - positionY;

        // Update all vertices based on the new position
        for (int i = 0; i < vertexCoords.length; i += 3) {
            vertexCoords[i] += dx;     // X coordinate
            vertexCoords[i + 1] += dy; // Y coordinate
        }

        positionX = x;
        positionY = y;

        setupVertexBuffer(); // Update buffer with new coordinates
    }

    public void draw() {
        int positionHandle = GLES20.glGetAttribLocation(shaderProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

        // Apply color to the fuselage, wings, and tail
        int colorHandle = GLES20.glGetUniformLocation(shaderProgram, "vColor");

        // Fuselage
        GLES20.glUniform4fv(colorHandle, 1, fuselageColor, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

        // Left wing
        GLES20.glUniform4fv(colorHandle, 1, wingColor, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 3, 3);

        // Right wing
        GLES20.glUniform4fv(colorHandle, 1, wingColor, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 6, 3);

        // Tail
        GLES20.glUniform4fv(colorHandle, 1, fuselageColor, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 9, 3);

        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}