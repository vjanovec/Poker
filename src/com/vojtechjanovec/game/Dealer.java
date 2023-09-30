package com.vojtechjanovec.game;

import com.vojtechjanovec.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Dealer {
    private List<Card> deck;
    private int bets;
    private int pot; // index

    public Dealer() {
        this.deck = newDeck();
        this.bets = 0;
        this.pot = 0;
//        deck.stream().forEach(c -> System.out.println(c));
    }

    public void dealCards(Game game) {
        System.out.println("Dealing cards");
        game.getPlayers().stream().forEach(p -> {
            for(int i=0; i<2; i++) {
                Card[] currentHand = p.getHand();
                currentHand[i] = this.deck.get(0);
                this.deck.remove(0);
                p.setHand(currentHand);
            }
            Arrays.asList(p.getHand()).stream().forEach(h -> System.out.println(h));
        });
    }

    public void dealChips(Game game) {
        System.out.println("Dealing chips");
        game.getPlayers().stream().forEach(p -> {
            p.setChip(5000);
        });
    }

    public List<Card> newDeck() {
        List<Card> newDeck = new ArrayList<>();
        for(Ranks rank : Ranks.values()) {
            for(Suits suit : Suits.values()) {
                newDeck.add(new Card(rank, suit));
            }
        }
        this.deck = newDeck;
        return newDeck;
    }

    public void shuffleDeck() {
        Collections.shuffle(deck);
    }

    public List<Card> getDeck() {
        return deck;
    }

    public int getBets() {
        return bets;
    }

    public int getPot() {
        return pot;
    }
}
