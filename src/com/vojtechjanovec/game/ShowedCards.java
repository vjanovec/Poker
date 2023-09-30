package com.vojtechjanovec.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class ShowedCards<Card> extends ArrayList<Card> implements Serializable {
    public ShowedCards(Collection c) {
        super(c);
    }

    public ShowedCards(int initialCapacity) {
        super(initialCapacity);
    }
}
