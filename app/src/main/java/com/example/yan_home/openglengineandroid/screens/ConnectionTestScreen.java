package com.example.yan_home.openglengineandroid.screens;

import com.example.yan_home.openglengineandroid.communication.socket.SocketConnectionManager;
import com.example.yan_home.openglengineandroid.entities.cards.Card;
import com.example.yan_home.openglengineandroid.entities.cards.CardsHelper;
import com.example.yan_home.openglengineandroid.input.cards.CardsTouchProcessor;
import com.example.yan_home.openglengineandroid.layouting.CardsLayoutSlot;
import com.example.yan_home.openglengineandroid.layouting.CardsLayouter;
import com.example.yan_home.openglengineandroid.layouting.impl.CardsLayouterImpl;
import com.example.yan_home.openglengineandroid.tweening.CardsTweenAnimator;
import com.yan.glengine.nodes.YANTexturedNode;
import com.yan.glengine.renderer.YANGLRenderer;
import com.yan.glengine.util.YANLogger;
import com.yan.glengine.util.math.YANMathUtils;

import java.util.ArrayList;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.TweenCallback;

/**
 * Created by Yan-Home on 10/3/2014.
 */
public class ConnectionTestScreen extends BaseGameScreen {

    private static final int CARDS_COUNT = 36;
    private static final int SCREEN_PADDING = 0;
    private static final int MAX_CARDS_IN_LINE = 8;

    private ArrayList<YANTexturedNode> mCardNodesArray;
    private ArrayList<YANTexturedNode> mNodesToRemove;
    private CardsTweenAnimator mCardsTweenAnimator;
    private CardsLayouter mCardsLayouter;
    private CardsTouchProcessor mCardsTouchProcessor;

    public ConnectionTestScreen(YANGLRenderer renderer) {
        super(renderer);
        mCardNodesArray = new ArrayList<>(CARDS_COUNT);
        mNodesToRemove = new ArrayList<>();
        mCardsLayouter = new CardsLayouterImpl(CARDS_COUNT);
        mCardsTweenAnimator = new CardsTweenAnimator();
        mCardsTouchProcessor = new CardsTouchProcessor(mCardNodesArray, mCardsTweenAnimator);
        mCardsTouchProcessor.setCardsTouchProcessorListener(new CardsTouchProcessor.CardsTouchProcessorListener() {
            @Override
            public void onSelectedCardTap(YANTexturedNode card) {
                removeCardFromHand(card);
            }

            @Override
            public void onDraggedCardReleased(YANTexturedNode card) {
                if (card.getPosition().getY() <= getSceneSize().getY() / 2) {
                    removeCardFromHand(card);
                } else {
                    layoutCards();
                }
            }
        });
    }


    @Override
    public void onSetActive() {
        super.onSetActive();
        mCardsTouchProcessor.register();
        SocketConnectionManager.getInstance().connectToRemoteServer("192.168.1.103", 7000);
    }

    @Override
    public void onSetNotActive() {
        super.onSetNotActive();
        mCardsTouchProcessor.unRegister();
        SocketConnectionManager.getInstance().disconnectFromRemoteServer();
    }

    @Override
    protected void onAddNodesToScene() {
        super.onAddNodesToScene();
        getNodeList().addAll(mCardNodesArray);
    }

    protected void onRemoveCardButtonClicked() {
        if (mCardNodesArray.isEmpty())
            return;

        //removing a random card from the hand
        final YANTexturedNode cardToRemove = mCardNodesArray.get((int) YANMathUtils.randomInRange(0, mCardNodesArray.size() - 1));
        removeCardFromHand(cardToRemove);
    }

    private void removeCardFromHand(final YANTexturedNode cardToRemove) {
        mCardNodesArray.remove(cardToRemove);
        float targetRotation = YANMathUtils.randomInRange(0, 360);

        float targetXPosition = getSceneSize().getX() / 2;
        float targetYPosition = 200;
        TweenCallback animationEndCallback = new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                cardToRemove.setSortingLayer(1);
            }
        };

        mCardsTweenAnimator.animateCardToValues(cardToRemove, targetXPosition, targetYPosition, targetRotation, animationEndCallback);
        mNodesToRemove.add(cardToRemove);
        layoutCards();
    }


    @Override
    protected void onLayoutNodes() {
        super.onLayoutNodes();
        layoutCards();
    }

    private void layoutCards() {

        mCardsLayouter.setActiveSlotsAmount(mCardNodesArray.size());

        //each index in cards array corresponds to slot index
        for (int i = 0; i < mCardNodesArray.size(); i++) {
            YANTexturedNode card = mCardNodesArray.get(i);
            CardsLayoutSlot slot = mCardsLayouter.getSlotAtPosition(i);
            card.setSortingLayer(slot.getSortingLayer());
            mCardsTweenAnimator.animateCardToSlot(card, slot);
        }
    }


    @Override
    protected void onChangeNodesSize() {
        super.onChangeNodesSize();

        if (mCardNodesArray.isEmpty())
            return;

        //cards
        float aspectRatio = mCardNodesArray.get(0).getTextureRegion().getWidth() / mCardNodesArray.get(0).getTextureRegion().getHeight();
        float cardWidth = Math.min(getSceneSize().getX(), getSceneSize().getY()) / (float) ((MAX_CARDS_IN_LINE) / 2);
        float cardHeight = cardWidth / aspectRatio;

        mCardsTouchProcessor.setOriginalCardSize(cardWidth, cardHeight);

        for (YANTexturedNode cardNode : mCardNodesArray) {
            cardNode.setSize(cardWidth, cardHeight);
        }

        //init the layouter
        mCardsLayouter.init(cardWidth, cardHeight,
                //maximum available width
                getSceneSize().getX() - (SCREEN_PADDING * 2),
                //maximum available height
                getSceneSize().getY(),
                //base x position ( center )
                getSceneSize().getX() / 2,
                //base y position
                getSceneSize().getY() - mFence.getSize().getY() / 2);
    }

    protected void onResetLayoutButtonClicked() {
        for (YANTexturedNode node : mNodesToRemove) {
            removeNode(node);
        }

        mNodesToRemove.clear();

        for (YANTexturedNode node : mCardNodesArray) {
            removeNode(node);
        }

        mCardNodesArray.clear();
        initCardsArray();
        for (YANTexturedNode node : mCardNodesArray) {
            addNode(node);
        }
        onChangeNodesSize();
        onLayoutNodes();
    }

    @Override
    protected void onCreateNodes() {
        super.onCreateNodes();
        initCardsArray();
    }

    private void initCardsArray() {
        ArrayList<Card> cardEntities = CardsHelper.create36Deck();
        for (Card cardEntity : cardEntities) {
            String name = "cards_" + cardEntity.getSuit() + "_" + cardEntity.getRank() + ".png";
            YANTexturedNode card = new YANTexturedNode(mAtlas.getTextureRegion(name));
            mCardNodesArray.add(card);
        }
    }

    @Override
    public void onUpdate(float deltaTimeSeconds) {
        super.onUpdate(deltaTimeSeconds);

        //read messages from remote socket server
        if (SocketConnectionManager.getInstance().isConnected()) {
            String msg = SocketConnectionManager.getInstance().readMessageFromRemoteServer();
            if (msg != null) {
                YANLogger.log("Message from socket server : " + msg);
            }
        }

        mCardsTweenAnimator.update(deltaTimeSeconds * 1);
    }

}