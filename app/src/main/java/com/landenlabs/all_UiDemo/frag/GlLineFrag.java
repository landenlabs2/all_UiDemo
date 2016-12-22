/**
 * Copyright (c) 2015 Dennis Lang (LanDen Labs) landenlabs@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
 * NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Dennis Lang  (3/21/2015)
 * @see http://landenlabs.com
 *
 */
package com.landenlabs.all_UiDemo.frag;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.landenlabs.all_UiDemo.R;
import com.landenlabs.all_UiDemo.Ui;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;


// import javax.microedition.khronos.opengles.GL10;


/**
 * Demonstrate Scrollview resizing with views above and below.
 *
 * @author Dennis Lang (LanDen Labs)
 * @see <a href="http://landenlabs.com/android/index-m.html"> author's web-site </a>
 */

public class GlLineFrag  extends UiFragment implements View.OnClickListener {

    View mRootView;
    GLSurfaceView mGlSurfaceView;

    // =============================================================================================

    static class Line {
        private FloatBuffer VertexBuffer;

        private final String VertexShaderCode =
                // This matrix member variable provides a hook to manipulate
                // the coordinates of the objects that use this vertex shader
                "uniform mat4 uMVPMatrix;" +

                        "attribute vec4 vPosition;" +
                        "void main() {" +
                        // the matrix must be included as a modifier of gl_Position
                        "  gl_Position = uMVPMatrix * vPosition;" +
                        "}";

        private final String FragmentShaderCode =
                "precision mediump float;" +
                        "uniform vec4 vColor;" +
                        "void main() {" +
                        "  gl_FragColor = vColor;" +
                        "}";

        protected int GlProgram;
        protected int PositionHandle;
        protected int ColorHandle;
        protected int MVPMatrixHandle;

        // number of coordinates per vertex in this array
        static final int COORDS_PER_VERTEX = 3;
        static float LineCoords[] = {
                0.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f
        };

        private final int VertexCount = LineCoords.length / COORDS_PER_VERTEX;
        private final int VertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

        // Set color with red, green, blue and alpha (opacity) values
        float color[] = { 0.0f, 0.0f, 0.0f, 1.0f };

        public Line() {
            // initialize vertex byte buffer for shape coordinates
            ByteBuffer bb = ByteBuffer.allocateDirect(
                    // (number of coordinate values * 4 bytes per float)
                    LineCoords.length * 4);
            // use the device hardware's native byte order
            bb.order(ByteOrder.nativeOrder());

            // create a floating point buffer from the ByteBuffer
            VertexBuffer = bb.asFloatBuffer();
            // add the coordinates to the FloatBuffer
            VertexBuffer.put(LineCoords);
            // set the buffer to read the first coordinate
            VertexBuffer.position(0);

            int vertexShader = GraphRenderer.loadShader(GLES20.GL_VERTEX_SHADER, VertexShaderCode);
            int fragmentShader = GraphRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, FragmentShaderCode);

            GlProgram = GLES20.glCreateProgram();             // create empty OpenGL ES Program
            GLES20.glAttachShader(GlProgram, vertexShader);   // add the vertex shader to program
            GLES20.glAttachShader(GlProgram, fragmentShader); // add the fragment shader to program
            GLES20.glLinkProgram(GlProgram);                  // creates OpenGL ES program executables
        }

        public void SetVerts(float v0, float v1, float v2, float v3, float v4, float v5) {
            LineCoords[0] = v0;
            LineCoords[1] = v1;
            LineCoords[2] = v2;
            LineCoords[3] = v3;
            LineCoords[4] = v4;
            LineCoords[5] = v5;

            VertexBuffer.put(LineCoords);
            // set the buffer to read the first coordinate
            VertexBuffer.position(0);
        }

        public void SetColor(float red, float green, float blue, float alpha) {
            color[0] = red;
            color[1] = green;
            color[2] = blue;
            color[3] = alpha;
        }

        public void draw(float[] mvpMatrix) {
            // Add program to OpenGL ES environment
            GLES20.glUseProgram(GlProgram);

            // get handle to vertex shader's vPosition member
            PositionHandle = GLES20.glGetAttribLocation(GlProgram, "vPosition");

            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(PositionHandle);

            // Prepare the triangle coordinate data
            GLES20.glVertexAttribPointer(PositionHandle, COORDS_PER_VERTEX,
                    GLES20.GL_FLOAT, false,
                    VertexStride, VertexBuffer);

            // get handle to fragment shader's vColor member
            ColorHandle = GLES20.glGetUniformLocation(GlProgram, "vColor");

            // Set color for drawing the triangle
            GLES20.glUniform4fv(ColorHandle, 1, color, 0);

            // get handle to shape's transformation matrix
            MVPMatrixHandle = GLES20.glGetUniformLocation(GlProgram, "uMVPMatrix");
            GraphRenderer.checkGlError("glGetUniformLocation");

            // Apply the projection and view transformation
            GLES20.glUniformMatrix4fv(MVPMatrixHandle, 1, false, mvpMatrix, 0);
            GraphRenderer.checkGlError("glUniformMatrix4fv");

            // Draw the triangle
            GLES20.glDrawArrays(GLES20.GL_LINES, 0, VertexCount);

            // Disable vertex array
            GLES20.glDisableVertexAttribArray(PositionHandle);
        }
    }

    // =============================================================================================
    static class GraphRenderer implements GLSurfaceView.Renderer {

        float[] mvpMatrix;

        public void onDrawFrame(GL10 gl) {
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();

            GLU.gluLookAt(gl, 0, 0, -5, 0f, 0f, 0f, 0f, 1.0f, 1.0f);
            gl.glColor4f(1, 0, 0, .5f);

            if (mvpMatrix == null) {
                mvpMatrix = new float[16];
            }
            // gl.glGetFloatv(GL_MODELVIEW_MATRIX, mvpMatrix);

            drawGraph(gl);
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            gl.glViewport(0, 0, width, height);

            float ratio = (float) width / height;
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glFrustumf(-ratio, ratio, -1, 1, 3, 7);
        }

        public void onSurfaceCreated(GL10 gl, javax.microedition.khronos.egl.EGLConfig config) {
        }

        private void drawGraph(GL10 gl) {
            gl.glLineWidth(1.0f);

            // http://stackoverflow.com/questions/16027455/what-is-the-easiest-way-to-draw-line-using-opengl-es-android

            Line vertLine = new Line();
            vertLine.SetVerts(-0.5f, 0.5f, 0f, -0.5f, -0.5f, 0f);
            vertLine.SetColor(.8f, .8f, 0f, 1.0f);
            // vertLine.draw(mvpMatrix);
        }

        public static void checkGlError(String msg) {

        }
        public static int loadShader(int type, String shaderCode) {
            int shader = GLES20.glCreateShader(type);
            GLES20.glShaderSource(shader, shaderCode);
            GLES20.glCompileShader(shader);
            return shader;
        }
    }

    // =============================================================================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.glline_frag, container, false);

        setup();
        return mRootView;
    }

    @Override
    public int getFragId() {
        return R.id.glcube_id;
    }

    @Override
    public String getName() {
        return "GlCube";
    }

    @Override
    public String getDescription() {
        return "??";
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            default:
                break;
        }
    }

    private void setup() {
        mGlSurfaceView = Ui.viewById(mRootView, R.id.gl_line_surfaceview);
        mGlSurfaceView.setRenderer(new GraphRenderer());

        // Ui.viewById(mRootView, R.id.scroll_add).setOnClickListener(this);
    }
}
