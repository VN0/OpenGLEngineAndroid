package com.example.yan_home.openglengineandroid.durak.local_server.commands.composite;


import com.example.yan_home.openglengineandroid.durak.local_server.cards.Card;
import com.example.yan_home.openglengineandroid.durak.local_server.cards.CardsHelper;
import com.example.yan_home.openglengineandroid.durak.local_server.cards.Pile;
import com.example.yan_home.openglengineandroid.durak.local_server.commands.BaseSessionCommand;
import com.example.yan_home.openglengineandroid.durak.local_server.commands.core.ResetGameDataCommand;
import com.example.yan_home.openglengineandroid.durak.local_server.commands.core.SelectTrumpCommand;
import com.example.yan_home.openglengineandroid.durak.local_server.commands.core.ShufflePileAtPositionCommand;
import com.example.yan_home.openglengineandroid.durak.local_server.commands.custom.AddPileCommand;

import java.util.ArrayList;

/**
 * Created by Yan-Home on 12/22/2014.
 *
 * This command clears the game session
 * and allocates discard and stock piles.
 *
 * All will be ready for players to join and start the game.
 */
public class PrepareGameSessionCommand extends BaseSessionCommand {
    @Override
    public void execute() {
        //reset game state
        getGameSession().executeCommand(new ResetGameDataCommand());

        //init stock pile
        AddPileCommand addStockPileCommand = new AddPileCommand();
        Pile stockPile = new Pile();
        stockPile.addTag(Pile.PileTags.STOCK_PILE_TAG);
        addStockPileCommand.setPile(stockPile);
        addStockPileCommand.setCards(CardsHelper.create36Deck());

        //add a stock pile
        getGameSession().executeCommand(addStockPileCommand);

        //init discard pile
        AddPileCommand addDiscardPileCommand = new AddPileCommand();
        Pile pile = new Pile();
        pile.addTag(Pile.PileTags.DISCARD_PILE_TAG);
        addDiscardPileCommand.setPile(pile);
        addDiscardPileCommand.setCards(new ArrayList<Card>());

        //add discard pile
        getGameSession().executeCommand(addDiscardPileCommand);

        int indexOfStockPile = getGameSession().getPilesStack().indexOf(getGameSession().findPileByTag(Pile.PileTags.STOCK_PILE_TAG));

        //shuffle the stock pile
        ShufflePileAtPositionCommand shufflePileCommand = new ShufflePileAtPositionCommand();
        shufflePileCommand.setPilePosition(indexOfStockPile);
        getGameSession().executeCommand(shufflePileCommand);

        //set trump
        SelectTrumpCommand selectTrumpCommand = new SelectTrumpCommand();
        selectTrumpCommand.setTrumpPileIndex(indexOfStockPile);
        getGameSession().executeCommand(selectTrumpCommand);
    }
}