package com.example.yan_home.openglengineandroid.durak.protocol.messages;

import com.example.yan_home.openglengineandroid.durak.entities.cards.Card;
import com.example.yan_home.openglengineandroid.durak.protocol.BaseProtocolMessage;
import com.example.yan_home.openglengineandroid.durak.protocol.data.CardData;
import com.google.gson.annotations.SerializedName;


/**
 * Created by Yan-Home on 12/24/2014.
 */
public class ResponseCardForAttackMessage extends BaseProtocolMessage<ResponseCardForAttackMessage.ProtocolMessageData> {

    public static final String MESSAGE_NAME = "responseCardForAttack";

    public ResponseCardForAttackMessage(Card cardForAttack) {
        super();
        setMessageName(MESSAGE_NAME);
        setMessageData(new ProtocolMessageData(new CardData(cardForAttack.getRank(), cardForAttack.getSuit())));
    }

    public static class ProtocolMessageData {
        @SerializedName("cardForAttack")
        CardData mCardForAttack;

        public ProtocolMessageData(CardData cardForAttack) {
            mCardForAttack = cardForAttack;
        }

        public CardData getCardForAttack() {
            return mCardForAttack;
        }
    }
}