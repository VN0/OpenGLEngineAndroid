package com.yan.glengine.tester.screens;

import aurelienribon.tweenengine.TweenManager;
import glengine.yan.glengine.assets.YANAssetManager;
import glengine.yan.glengine.assets.atlas.YANTextureAtlas;
import glengine.yan.glengine.nodes.YANButtonNode;
import glengine.yan.glengine.nodes.YANTextNode;
import glengine.yan.glengine.renderer.YANGLRenderer;
import glengine.yan.glengine.screens.YANIScreen;
import glengine.yan.glengine.screens.YANNodeScreen;
import glengine.yan.glengine.service.ServiceLocator;
import glengine.yan.glengine.util.colors.YANColor;
import glengine.yan.glengine.util.loggers.YANFPSLogger;

/**
 * Created by Yan-Home on 1/17/2015.
 * <p/>
 * This screen is a base for other screens that represents a showcase
 * of the engine.
 */
public abstract class BaseTestScreen extends YANNodeScreen {

    protected static final int OVERLAY_SORTING_LAYER = 1000;

    protected TweenManager mTweenManager;
    protected YANTextureAtlas mUiAtlas;
    protected YANTextureAtlas mCardsAtlas;

    //Used to log FPS data on screen
    YANTextNode mFpsTextNode;
    YANFPSLogger mFPSLogger;

    //arrows to switch between screens
    YANButtonNode mRightButton;
    YANButtonNode mLeftButton;

    //Used to navvigate between screens
    protected YANIScreen mPreviousScreen;
    protected YANIScreen mNextScreen;

    public BaseTestScreen(YANGLRenderer renderer) {
        super(renderer);

        mTweenManager = new TweenManager();

        //set gray background
        renderer.setRendererBackgroundColor(YANColor.createFromHexColor(0xa9a9a9));

        //setup fps logger
        mFPSLogger = new YANFPSLogger();
        mFPSLogger.setFPSLoggerListener(new YANFPSLogger.FPSLoggerListener() {
            @Override
            public void onValueChange(int newFpsValue) {
                mFpsTextNode.setText("FPS " + newFpsValue);
            }
        });

        mUiAtlas = ServiceLocator.locateService(YANAssetManager.class).getLoadedAtlas("ui_atlas");
        mCardsAtlas = ServiceLocator.locateService(YANAssetManager.class).getLoadedAtlas("cards_atlas");
    }

    @Override
    public void onSetActive() {
        super.onSetActive();
        //load atlas into a memory
        ServiceLocator.locateService(YANAssetManager.class).loadTexture(ServiceLocator.locateService(YANAssetManager.class).getLoadedAtlas("ui_atlas").getAtlasImageFilePath());
        ServiceLocator.locateService(YANAssetManager.class).loadTexture(ServiceLocator.locateService(YANAssetManager.class).getLoadedAtlas("cards_atlas").getAtlasImageFilePath());
        //load font atlas into a memory
        ServiceLocator.locateService(YANAssetManager.class).loadTexture(ServiceLocator.locateService(YANAssetManager.class).getLoadedFont("standard_font").getGlyphImageFilePath());

        mNextScreen = onSetNextScreen();
        mPreviousScreen = onSetPreviousScreen();
    }


    @Override
    public void onSetNotActive() {
        super.onSetNotActive();
        //remove all animations
        mTweenManager.killAll();

        //release atlas from a memory
        ServiceLocator.locateService(YANAssetManager.class).unloadTexture(ServiceLocator.locateService(YANAssetManager.class).getLoadedAtlas("ui_atlas").getAtlasImageFilePath());
        //release atlas font from a memory
        ServiceLocator.locateService(YANAssetManager.class).unloadTexture(ServiceLocator.locateService(YANAssetManager.class).getLoadedFont("standard_font").getGlyphImageFilePath());
    }

    @Override
    public void onUpdate(float deltaTimeSeconds) {
        super.onUpdate(deltaTimeSeconds);

        mTweenManager.update(deltaTimeSeconds);

        //update logger value
        mFPSLogger.update(deltaTimeSeconds);
    }

    @Override
    protected void onAddNodesToScene() {
        addNode(mFpsTextNode);
        addNode(mRightButton);
        addNode(mLeftButton);
    }

    @Override
    protected void onLayoutNodes() {

        //fps node
        mFpsTextNode.setPosition(50, getSceneSize().getY() * 0.9f);

        //button nodes
        mRightButton.setPosition(getSceneSize().getX() - mRightButton.getSize().getX(), (getSceneSize().getY() * 1) - mRightButton.getSize().getY());
        mLeftButton.setPosition(mRightButton.getPosition().getX() - mLeftButton.getSize().getX(), mRightButton.getPosition().getY());
    }

    @Override
    protected void onChangeNodesSize() {
        float buttonsSize = getSceneSize().getX() * 0.2f;
        mRightButton.setSize(buttonsSize, buttonsSize);
        mLeftButton.setSize(buttonsSize, buttonsSize);
    }

    @Override
    protected void onCreateNodes() {
        //create a text node
        mFpsTextNode = new YANTextNode(ServiceLocator.locateService(YANAssetManager.class).getLoadedFont("standard_font"), "FPS 100".length());
        mFpsTextNode.setText("FPS " + 0);
        mFpsTextNode.setSortingLayer(OVERLAY_SORTING_LAYER);

        //create right arrow
        mRightButton = new YANButtonNode(mUiAtlas.getTextureRegion("arrow_right.png"), mUiAtlas.getTextureRegion("arrow_right_pressed.png"));
        mRightButton.setSortingLayer(OVERLAY_SORTING_LAYER);
        mRightButton.setClickListener(new YANButtonNode.YanButtonNodeClickListener() {
            @Override
            public void onButtonClick() {
                goToNextScreen();
            }
        });

        //create left arrow
        mLeftButton = new YANButtonNode(mUiAtlas.getTextureRegion("arrow_right.png"), mUiAtlas.getTextureRegion("arrow_right_pressed.png"));
        mLeftButton.setSortingLayer(OVERLAY_SORTING_LAYER);
        mLeftButton.setRotationZ(180);
        mLeftButton.setClickListener(new YANButtonNode.YanButtonNodeClickListener() {
            @Override
            public void onButtonClick() {
                goToPreviousScreen();
            }
        });
    }

    private void goToPreviousScreen() {
        if (mPreviousScreen == null)
            return;
        getRenderer().setActiveScreen(mPreviousScreen);
    }

    private void goToNextScreen() {
        if (mNextScreen == null)
            return;
        getRenderer().setActiveScreen(mNextScreen);
    }

    protected abstract YANIScreen onSetNextScreen();

    protected abstract YANIScreen onSetPreviousScreen();

}