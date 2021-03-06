package glengine.yan.glengine.tween;

import glengine.yan.glengine.nodes.YANIRenderableNode;

import aurelienribon.tweenengine.TweenAccessor;

/**
 * Created by Yan-Home on 10/25/2014.
 */
public class YANTweenNodeAccessor implements TweenAccessor<YANIRenderableNode> {

    public static final int POSITION_X = 1;
    public static final int POSITION_Y = 2;
    public static final int POSITION_XY = 3;
    public static final int ROTATION_Z_CW = 4;
    public static final int OPACITY = 5;
    public static final int SIZE_X = 6;
    public static final int SIZE_Y = 7;
    public static final int SIZE_XY = 8;

    @Override
    public int getValues(YANIRenderableNode target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case POSITION_X:
                returnValues[0] = target.getPosition().getX();
                return 1;
            case POSITION_Y:
                returnValues[0] = target.getPosition().getY();
                return 1;

            case POSITION_XY:
                returnValues[0] = target.getPosition().getX();
                returnValues[1] = target.getPosition().getY();
                return 2;

            case SIZE_XY:
                returnValues[0] = target.getSize().getX();
                returnValues[1] = target.getSize().getY();
                return 2;

            case ROTATION_Z_CW:
                returnValues[0] = target.getRotationZ();
                return 1;

            case OPACITY:
                returnValues[0] = target.getOpacity();
                return 1;

            case SIZE_X:
                returnValues[0] = target.getSize().getX();
                return 1;

            case SIZE_Y:
                returnValues[0] = target.getSize().getY();
                return 1;
            default:
                assert false;
                return -1;
        }
    }

    @Override
    public void setValues(YANIRenderableNode target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case POSITION_X:
                target.setPosition(newValues[0],target.getPosition().getY());
                break;
            case POSITION_Y:
                target.setPosition(target.getPosition().getX(),newValues[0]);
                break;
            case POSITION_XY:
                target.setPosition(newValues[0],newValues[1]);
                break;

            case SIZE_XY:
                target.setSize(newValues[0], newValues[1]);
                break;

            case ROTATION_Z_CW:
                target.setRotationZ(newValues[0]);
                break;
            case OPACITY:
                target.setOpacity(newValues[0]);
                break;

            case SIZE_X:
                target.setSize(newValues[0], target.getSize().getY());
                break;

            case SIZE_Y:
                target.setSize(target.getSize().getX(), newValues[0]);
                break;

            default:
                assert false;
                break;
        }
    }
}
