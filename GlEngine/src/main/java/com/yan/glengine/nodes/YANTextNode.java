package com.yan.glengine.nodes;

import com.yan.glengine.data.YANVertexArray;
import com.yan.glengine.programs.ShaderProgram;
import com.yan.glengine.util.math.YANRectangle;
import com.yan.glengine.util.math.YANVector2;

/**
 * Created by Yan-Home on 10/26/2014.
 */
public class YANTextNode implements YANIRenderableNode {

    private YANVertexArray mVertexArray;

    public YANTextNode() {
        mVertexArray = new YANVertexArray(createVertexData());
    }

    private float[] createVertexData() {

        //TODO : create data form mnager
        return new float[0];
    }

    @Override
    public void bindData(ShaderProgram shaderProgram) {
        //TODO : bind vertex data to
    }

    @Override
    public void draw() {

    }

    @Override
    public YANVector2 getPosition() {
        return null;
    }

    @Override
    public YANVector2 getSize() {
        return null;
    }

    @Override
    public void setSize(YANVector2 size) {

    }

    @Override
    public YANRectangle getBoundingRectangle() {
        return null;
    }

    @Override
    public void onAttachedToScreen() {

    }

    @Override
    public void onDetachedFromScreen() {

    }

    @Override
    public YANVector2 getAnchorPoint() {
        return null;
    }

    @Override
    public float getRotation() {
        return 0;
    }

    @Override
    public void setRotation(float rotation) {

    }

    @Override
    public float getOpacity() {
        return 0;
    }

    @Override
    public void setOpacity(float opacity) {

    }
}