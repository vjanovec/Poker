package com.vojtechjanovec;

import com.vojtechjanovec.communication.Actions;
import com.vojtechjanovec.communication.Options;
import com.vojtechjanovec.communication.Response;
import com.vojtechjanovec.game.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {

	// write your code here
//        System.out.println(Ranks.Ace.value);
//        Ranks ace, king;
//        ace = Ranks.Ace;
//        king = Ranks.King;
//        System.out.println(king.compareTo(ace));
//        Card card = new Card(Ranks.Ace, Suits.CLUB);
//        System.out.println(card.toString());
//        Dealer d = new Dealer();
//        d.shuffleDeck();
//        for(Card c : d.getDeck()) {
//            System.out.println(c);
//        }
//        List<Player> players = new LinkedList<Player>(Arrays.asList(new Player("PRVNI", 5000), new Player("DRUHY", 5000)));
//
//
//        List<Player> players = new ArrayList<>(Arrays.asList(new Player("PRVNI", 5000), new Player("DRUHY", 5000)));
//        Game game = new Game(players);
//        game.start();

//            Response r = new Response(Actions.FOLD);
//        System.out.println(r.getClass().getName());


//        Action a = Action.CHECK;
//        System.out.println(a.toString());

//        Client c = new Client("127.0.0.1", 8086);
        Options o = new Options(new ArrayList<Actions>(Collections.singleton(Actions.FOLD)), 5);
        System.out.println(o.getClass().getName());
        String s = "fdsf";
        System.out.println(s.getClass().getName());
        Card card = new Card(Ranks.Ace, Suits.CLUB);
        Card[] c2 = { card, card};
        System.out.println(card.getClass().getName());
        System.out.println(c2.getClass().getName());
        Response r = new Response(Actions.FOLD, 100);
        System.out.println(r.getClass().getName());
        System.out.println(r.getAction().toString());

        ShowedCards<Card> showedCards = new ShowedCards<>(Arrays.asList(new Card(Ranks.Ace, Suits.CLUB)));
        System.out.println(showedCards.getClass().getName());
    }
}
