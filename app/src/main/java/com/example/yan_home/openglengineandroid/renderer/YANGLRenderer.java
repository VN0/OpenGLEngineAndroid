package com.example.yan_home.openglengineandroid.renderer;

import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.example.yan_home.openglengineandroid.GLEngineApp;
import com.example.yan_home.openglengineandroid.assets.YANAssetManager;
import com.example.yan_home.openglengineandroid.nodes.YANButtonNode;
import com.example.yan_home.openglengineandroid.nodes.YANIRenderableNode;
import com.example.yan_home.openglengineandroid.nodes.YANTexturedNode;
import com.example.yan_home.openglengineandroid.programs.YANColorShaderProgram;
import com.example.yan_home.openglengineandroid.programs.YANTextureShaderProgram;
import com.example.yan_home.openglengineandroid.screens.YANIScreen;
import com.example.yan_home.openglengineandroid.screens.impl.YANButtonTestScreen;
import com.example.yan_home.openglengineandroid.util.YANMatrixHelper;
import com.example.yan_home.openglengineandroid.util.colors.YANColor;
import com.example.yan_home.openglengineandroid.util.math.Vector2;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Yan-Home on 10/3/2014.
 * <p/>
 * Implementer of graphics rendering using openGL
 */
public class YANGLRenderer implements IRenderer {

    private YANIScreen mCurrentScreen;
    private Vector2 mSurfaceSize;

    // shader programs
    private YANTextureShaderProgram textureProgram;
    private YANColorShaderProgram colorProgram;
    private float mPreviousFrameTime;

    private YANIScreen getStartingScreen() {
        return new YANButtonTestScreen(this);
//        return new YANTouchTestScreen(this);
    }

    @Override
    public void onGLSurfaceCreated() {
        // Enable blending using pre-multiplied alpha.
        setGlInitialStates();
        loadShaderPrograms();

        mPreviousFrameTime = System.currentTimeMillis();
    }

    @Override
    public void onGLSurfaceChanged(int width, int height) {
        // Set the OpenGL viewport to fill the entire surface.
        GLES20.glViewport(0, 0, width, height);

        //the size of the surface will be used by each screen
        mSurfaceSize = new Vector2(width, height);

        //when context is recreated all previously loaded textures must be cleaned.
        YANAssetManager.getInstance().reloadAllLoadedTextures();

        //set orthographic projection
        Matrix.orthoM(YANMatrixHelper.projectionMatrix, 0, 0, width, height,0 , 1, 100);

        //fill view matrix
        Matrix.setLookAtM(YANMatrixHelper.viewMatrix, 0, 0f, 0.0f, 2.0f, 0f, 0f, 0f, 0f, 1f, 0f);

        if (mCurrentScreen == null) {
            setActiveScreen(getStartingScreen());
        } else {
            //call screen on resize method
            mCurrentScreen.onResize(mSurfaceSize.getX(), mSurfaceSize.getY());
        }
    }

    @Override
    public void onDrawFrame() {

        //update screen state
        mCurrentScreen.onUpdate(System.currentTimeMillis() - mPreviousFrameTime);

        // Clear the rendering surface.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Update the viewProjection matrix, and create an inverted matrix for
        // touch picking.
        Matrix.multiplyMM(YANMatrixHelper.viewProjectionMatrix, 0, YANMatrixHelper.projectionMatrix, 0, YANMatrixHelper.viewMatrix, 0);
        Matrix.invertM(YANMatrixHelper.invertedViewProjectionMatrix, 0, YANMatrixHelper.viewProjectionMatrix, 0);

        //draw each node
        drawNodes();

        mPreviousFrameTime = System.currentTimeMillis();
    }

    private void drawNodes() {
        for (YANIRenderableNode iNode : mCurrentScreen.getNodeList()) {
            YANMatrixHelper.positionObjectInScene(iNode);

            if (iNode instanceof YANTexturedNode) {
                textureProgram.useProgram();
                textureProgram.setUniforms(YANMatrixHelper.modelViewProjectionMatrix, YANAssetManager.getInstance().getLoadedTextureHandle(((YANTexturedNode) iNode).getTexture()));
            } else if (iNode instanceof YANButtonNode) {
                textureProgram.useProgram();
                textureProgram.setUniforms(YANMatrixHelper.modelViewProjectionMatrix, YANAssetManager.getInstance().getLoadedTextureHandle(((YANButtonNode) iNode).getCurrentStateTexture()));
            } else {
                throw new RuntimeException("Don't know how to render node of type " + iNode.getClass().getSimpleName());
            }

            iNode.bindData(textureProgram);
            iNode.draw();
        }
    }

    private void setGlInitialStates() {

        //clear color
        YANColor color = YANColor.createFromHexColor(Color.GRAY);
        GLES20.glClearColor(color.getR(), color.getG(), color.getB(), color.getA());

        //enable alpha blending
        GLES20.glEnable(GL10.GL_BLEND);
        GLES20.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void loadShaderPrograms() {
        textureProgram = new YANTextureShaderProgram(GLEngineApp.getAppContext());
        colorProgram = new YANColorShaderProgram(GLEngineApp.getAppContext());
    }

    public void setActiveScreen(YANIScreen screen) {
        if (mCurrentScreen != null) {
            mCurrentScreen.onSetNotActive();
        }
        mCurrentScreen = screen;
        mCurrentScreen.onSetActive();
    }

    @Override
    public Vector2 getSurfaceSize() {
        return mSurfaceSize;
    }
}
