package com.vojtechjanovec.game;

public enum Ranks {
    Two(1, "Two"),
    Three(2, "Three"),
    Four(3, "Four"),
    Five(4, "Five"),
    Six(5, "Six"),
    Seven(6, "Seven"),
    Eight(7,"Eight"),
    Nine(8, "Nine"),
    Ten(9, "Ten"),
    Jack(10, "Jack"),
    Queen(11, "Queen"),
    King(12, "King"),
    Ace(13, "Ace");

    public final int value;
    public final String name;
    private Ranks(int value, String name) {
        this.value = value;
        this.name = name;
    }
}
