package com.vojtechjanovec.game;

import com.vojtechjanovec.communication.Actions;

public class Player {
    private final String userName;
    private Card[] hand;
    private Actions currentAction;
    private int chip;
    private HandRankings hr;


    public Player(String userName, Card[] hand, int chip) {
        this.userName = userName;
        this.hand = hand;
        this.currentAction = Actions.NONE;
        this.chip = chip;

    }
    public Player(String userName, int chip) {
        this.userName = userName;
        this.hand = new Card[2];
        this.currentAction = Actions.NONE;
        this.chip = chip;
    }

    public Player(String userName) {
        this.userName = userName;
        this.hand = new Card[2];
    }

    // ACTIONS
    public void fold() {
        this.currentAction = Actions.FOLD;
    }
    public void check() {
        this.currentAction = Actions.CHECK;
    }
    public void raise(int value) {
        this.currentAction = Actions.RAISE;
        this.chip -= value;
    }

    public void setHand(Card[] hand) {
        this.hand = hand;
    }

    public Card[] getHand() {
        return hand;
    }

    public Actions getCurrentAction() {
        return currentAction;
    }

    public int getChip() {
        return chip;
    }

    public void setCurrentAction(Actions currentAction) {
        this.currentAction = currentAction;
    }

    public String getUserName() {
        return userName;
    }

    public void setChip(int chip) {
        this.chip = chip;
    }

    public HandRankings getHr() {
        return hr;
    }

    public void setHr(HandRankings hr) {
        this.hr = hr;
    }
}
