package com.example.yan_home.openglengineandroid.durak.screen_fragments.cards;

import com.example.yan_home.openglengineandroid.durak.entities.cards.Card;
import com.example.yan_home.openglengineandroid.durak.nodes.CardNode;
import com.example.yan_home.openglengineandroid.durak.screen_fragments.IScreenFragment;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Yan-Home on 1/29/2015.
 */
public interface ICardsScreenFragment extends IScreenFragment {

    int getBottomPlayerPileIndex();
    int getTopRightPlayerPileIndex();
    int getTopLeftPlayerPileIndex();

    /**
     * Returns a card that is underlying under provided card or
     * null if card not found.
     */
    CardNode findUnderlyingCard(CardNode cardNode);

    /**
     * Goes over all cards and removes tags from them
     */
    void removeTagsFromCards();

    public interface ICardMovementListener {
        void onCardMovesToBottomPlayerPile();

        void onCardMovesToTopRightPlayerPile();

        void onCardMovesToTopLeftPlayerPile();

        void onCardMovesToFieldPile();
    }

    void setCardMovementListener(ICardMovementListener listener);

    void moveCardFromPileToPile(Card movedCard, int fromPile, int toPile);

    float getCardNodeWidth();

    float getCardNodeHeight();

    int getTotalCardsAmount();

    ArrayList<CardNode> getBottomPlayerCardNodes();

    ArrayList<CardNode> getTopRightPlayerCardNodes();

    ArrayList<CardNode> getTopLeftPlayerCardNodes();

    void setPilesIndexes(int stockPileIndex, int discardPileIndex, int bottomPlayerPileIndex, int topPlayerToTheRightPileIndex, int topLeftPlayerToTheLeftPileIndex);

    void setTrumpCard(Card card);

    Collection<Card> getCardsInPileWithIndex(int pileIndex);
}
