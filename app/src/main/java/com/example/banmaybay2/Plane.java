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

    private float[] vertexCoords;
    private float[] fuselageColor = {0.6f, 0.7f, 0.2f, 1.0f}; // Green
    private float[] wingColor = {0.8f, 0.8f, 0.1f, 1.0f};     // Yellow
    private float[] tailColor = {0.9f, 0.1f, 0.1f, 1.0f};      // Red

    private float positionX = 0.0f, positionY = 0.0f;

    public Plane(int shaderProgram) {
        this.shaderProgram = shaderProgram;
        vertexCoords = new float[] {
                // Fuselage (body)
                0.0f,  0.2f, 0.0f,    // top
                -0.05f, -0.1f, 0.0f,  // bottom left
                0.05f, -0.1f, 0.0f,   // bottom right

                // Left wing
                -0.15f, 0.0f, 0.0f,   // left wing top
                -0.3f, -0.1f, 0.0f,   // left wing bottom left
                -0.15f, -0.2f, 0.0f,  // left wing bottom right

                // Right wing
                0.15f, 0.0f, 0.0f,    // right wing top
                0.3f, -0.1f, 0.0f,    // right wing bottom left
                0.15f, -0.2f, 0.0f,   // right wing bottom right

                // Tail
                0.0f,  0.3f, 0.0f,    // tail top
                -0.05f, 0.2f, 0.0f,   // tail bottom left
                0.05f, 0.2f, 0.0f     // tail bottom right
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
        this.positionX = x;
        this.positionY = y;

        // Update all vertices based on new position
        // Fuselage (center body)
        vertexCoords[0] = positionX; vertexCoords[1] = positionY + 0.2f;
        vertexCoords[3] = positionX - 0.05f; vertexCoords[4] = positionY - 0.1f;
        vertexCoords[6] = positionX + 0.05f; vertexCoords[7] = positionY - 0.1f;

        // Left wing
        vertexCoords[9] = positionX - 0.15f; vertexCoords[10] = positionY + 0.0f;
        vertexCoords[12] = positionX - 0.3f; vertexCoords[13] = positionY - 0.1f;
        vertexCoords[15] = positionX - 0.15f; vertexCoords[16] = positionY - 0.2f;

        // Right wing
        vertexCoords[18] = positionX + 0.15f; vertexCoords[19] = positionY + 0.0f;
        vertexCoords[21] = positionX + 0.3f; vertexCoords[22] = positionY - 0.1f;
        vertexCoords[24] = positionX + 0.15f; vertexCoords[25] = positionY - 0.2f;

        // Tail
        vertexCoords[27] = positionX; vertexCoords[28] = positionY + 0.3f;
        vertexCoords[30] = positionX - 0.05f; vertexCoords[31] = positionY + 0.2f;
        vertexCoords[33] = positionX + 0.05f; vertexCoords[34] = positionY + 0.2f;

        setupVertexBuffer(); // Update buffer with new coordinates
    }

    public void draw() {
        int positionHandle = GLES20.glGetAttribLocation(shaderProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

        // Apply color to the fuselage, wings, and tail
        // Fuselage Color
        int colorHandle = GLES20.glGetUniformLocation(shaderProgram, "vColor");
        GLES20.glUniform4fv(colorHandle, 1, fuselageColor, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3); // Drawing the fuselage part

        // Left Wing Color
        GLES20.glUniform4fv(colorHandle, 1, wingColor, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 3, 3); // Drawing left wing part

        // Right Wing Color
        GLES20.glUniform4fv(colorHandle, 1, wingColor, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 6, 3); // Drawing right wing part

        // Tail Color
        GLES20.glUniform4fv(colorHandle, 1, tailColor, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 9, 3); // Drawing tail part

        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
