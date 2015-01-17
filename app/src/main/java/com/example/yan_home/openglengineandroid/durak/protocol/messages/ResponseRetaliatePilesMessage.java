package com.example.yan_home.openglengineandroid.durak.protocol.messages;

import com.example.yan_home.openglengineandroid.durak.entities.cards.Card;
import com.example.yan_home.openglengineandroid.durak.protocol.BaseProtocolMessage;
import com.example.yan_home.openglengineandroid.durak.protocol.data.CardData;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yan-Home on 12/24/2014.
 */
public class ResponseRetaliatePilesMessage extends BaseProtocolMessage<ResponseRetaliatePilesMessage.ProtocolMessageData> {

    public static final String MESSAGE_NAME = "responseRetaliatePilesMessage";

    public ResponseRetaliatePilesMessage(List<List<Card>> pilesAfterRetaliation) {
        super();
        setMessageName(MESSAGE_NAME);
        List<List<CardData>> piles = convertCardDataList(pilesAfterRetaliation);
        setMessageData(new ProtocolMessageData(piles));
    }

    private List<List<CardData>> convertCardDataList(List<List<Card>> pilesAfterRetaliation) {
        List<List<CardData>> ret = new ArrayList<>();
        for (List<Card> list : pilesAfterRetaliation) {
            List<CardData> dataList = new ArrayList<>();
            for (Card card : list) {
                dataList.add(new CardData(card.getRank(),card.getSuit()));
            }
            ret.add(dataList);
        }
        return ret;
    }

    public static class ProtocolMessageData {
        @SerializedName("pilesAfterRetaliation")
        List<List<CardData>> mPilesAfterRetaliation;

        public ProtocolMessageData(List<List<CardData>> piles) {
            mPilesAfterRetaliation = piles;
        }

        public List<List<CardData>> getPilesAfterRetaliation() {
            return mPilesAfterRetaliation;
        }
    }
}