package com.vojtechjanovec.test;

import com.vojtechjanovec.game.Card;
import com.vojtechjanovec.game.HandRankings;
import com.vojtechjanovec.game.Ranks;
import com.vojtechjanovec.game.Suits;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HandRankingsTest {

    @org.junit.jupiter.api.Test
    void getRanking() {
        List<Card> cards = Arrays.asList(
                new Card(Ranks.Four, Suits.DIAMOND),
                new Card(Ranks.Four, Suits.CLUB),
                new Card(Ranks.Ace, Suits.HEARTH),
                new Card(Ranks.Jack, Suits.DIAMOND),
                new Card(Ranks.Ten, Suits.SPADE),
                new Card(Ranks.Three, Suits.CLUB),
                new Card(Ranks.Five, Suits.SPADE));
        assertEquals(HandRankings.PAIR, HandRankings.getRanking(cards));

        List<Card> cards2 = Arrays.asList(
                new Card(Ranks.Four, Suits.DIAMOND),
                new Card(Ranks.Four, Suits.CLUB),
                new Card(Ranks.Four, Suits.HEARTH),
                new Card(Ranks.Jack, Suits.DIAMOND),
                new Card(Ranks.Ten, Suits.SPADE),
                new Card(Ranks.Three, Suits.CLUB),
                new Card(Ranks.Five, Suits.SPADE));

        assertEquals(HandRankings.THREE_OF_A_KIND, HandRankings.getRanking(cards2));

        List<Card> cards3 = Arrays.asList(
                new Card(Ranks.Four, Suits.DIAMOND),
                new Card(Ranks.Four, Suits.CLUB),
                new Card(Ranks.Four, Suits.HEARTH),
                new Card(Ranks.Four, Suits.DIAMOND),
                new Card(Ranks.Ten, Suits.SPADE),
                new Card(Ranks.Three, Suits.CLUB),
                new Card(Ranks.Five, Suits.SPADE));

        assertEquals(HandRankings.FOUR_OF_A_KIND, HandRankings.getRanking(cards3));

        List<Card> cards4 = Arrays.asList(
                new Card(Ranks.Two, Suits.DIAMOND),
                new Card(Ranks.Four, Suits.DIAMOND),
                new Card(Ranks.Ace, Suits.SPADE),
                new Card(Ranks.Six, Suits.DIAMOND),
                new Card(Ranks.Ten, Suits.DIAMOND),
                new Card(Ranks.Three, Suits.CLUB),
                new Card(Ranks.Five, Suits.SPADE));

        assertEquals(HandRankings.STRAIGHT, HandRankings.getRanking(cards4));

        List<Card> cards5 = Arrays.asList(
                new Card(Ranks.Four, Suits.DIAMOND),
                new Card(Ranks.Four, Suits.DIAMOND),
                new Card(Ranks.Ace, Suits.DIAMOND),
                new Card(Ranks.Jack, Suits.DIAMOND),
                new Card(Ranks.Three, Suits.DIAMOND),
                new Card(Ranks.Five, Suits.SPADE),
                new Card(Ranks.Three, Suits.CLUB));

        assertEquals(HandRankings.FLUSH, HandRankings.getRanking(cards5));

    }

    @org.junit.jupiter.api.Test
    void getRankHandRanking() {
        List<Card> cards = Arrays.asList(
                new Card(Ranks.Four, Suits.DIAMOND),
                new Card(Ranks.Four, Suits.CLUB),
                new Card(Ranks.Ace, Suits.HEARTH),
                new Card(Ranks.Jack, Suits.DIAMOND),
                new Card(Ranks.Ten, Suits.SPADE),
                new Card(Ranks.Three, Suits.CLUB),
                new Card(Ranks.Five, Suits.SPADE));
        assertEquals(HandRankings.PAIR, HandRankings.getRanking(cards));

        // TODO: FIX
//        List<Card> cards2 = Arrays.asList(
//                new Card(Ranks.Four, Suits.DIAMOND),
//                new Card(Ranks.Four, Suits.CLUB),
//                new Card(Ranks.Jack, Suits.HEARTH),
//                new Card(Ranks.Jack, Suits.DIAMOND),
//                new Card(Ranks.Ten, Suits.SPADE),
//                new Card(Ranks.Three, Suits.CLUB),
//                new Card(Ranks.Five, Suits.SPADE));
//
//        assertEquals(HandRankings.TWO_PAIR, HandRankings.getRanking(cards2));


        List<Card> cards3 = Arrays.asList(
                new Card(Ranks.Four, Suits.DIAMOND),
                new Card(Ranks.Four, Suits.CLUB),
                new Card(Ranks.Four, Suits.HEARTH),
                new Card(Ranks.Four, Suits.DIAMOND),
                new Card(Ranks.Ten, Suits.SPADE),
                new Card(Ranks.Three, Suits.CLUB),
                new Card(Ranks.Five, Suits.SPADE));

        assertEquals(HandRankings.FOUR_OF_A_KIND, HandRankings.getRanking(cards3));

        // TODO: FIX
//        List<Card> cards4 = Arrays.asList(
//                new Card(Ranks.Four, Suits.DIAMOND),
//                new Card(Ranks.Four, Suits.CLUB),
//                new Card(Ranks.Jack, Suits.HEARTH),
//                new Card(Ranks.Jack, Suits.DIAMOND),
//                new Card(Ranks.Four, Suits.SPADE),
//                new Card(Ranks.Three, Suits.CLUB),
//                new Card(Ranks.Five, Suits.SPADE));
//
//        assertEquals(HandRankings.FULL_HOUSE, HandRankings.getRanking(cards4));



    }

    @org.junit.jupiter.api.Test
    void hasStraight() {
        List<Card> cards4 = Arrays.asList(
                new Card(Ranks.Two, Suits.DIAMOND),
                new Card(Ranks.Four, Suits.DIAMOND),
                new Card(Ranks.Ace, Suits.SPADE),
                new Card(Ranks.Six, Suits.DIAMOND),
                new Card(Ranks.Ten, Suits.DIAMOND),
                new Card(Ranks.Three, Suits.CLUB),
                new Card(Ranks.Five, Suits.SPADE));

        assertEquals(true, HandRankings.hasStraight(cards4));

        List<Card> cards3 = Arrays.asList(
                new Card(Ranks.Four, Suits.DIAMOND),
                new Card(Ranks.Four, Suits.CLUB),
                new Card(Ranks.Four, Suits.HEARTH),
                new Card(Ranks.Four, Suits.DIAMOND),
                new Card(Ranks.Ten, Suits.SPADE),
                new Card(Ranks.Three, Suits.CLUB),
                new Card(Ranks.Five, Suits.SPADE));

        assertEquals(false, HandRankings.hasStraight(cards3));

    }

    @org.junit.jupiter.api.Test
    void hasFlush() {
        List<Card> cards4 = Arrays.asList(
                new Card(Ranks.Two, Suits.DIAMOND),
                new Card(Ranks.Four, Suits.DIAMOND),
                new Card(Ranks.Ace, Suits.DIAMOND),
                new Card(Ranks.Six, Suits.DIAMOND),
                new Card(Ranks.Ten, Suits.DIAMOND),
                new Card(Ranks.Ten, Suits.CLUB),
                new Card(Ranks.Five, Suits.SPADE));

        assertEquals(true, HandRankings.hasFlush(cards4));

        List<Card> cards = Arrays.asList(
                new Card(Ranks.Four, Suits.DIAMOND),
                new Card(Ranks.Four, Suits.CLUB),
                new Card(Ranks.Ace, Suits.HEARTH),
                new Card(Ranks.Jack, Suits.DIAMOND),
                new Card(Ranks.Ten, Suits.SPADE),
                new Card(Ranks.Three, Suits.CLUB),
                new Card(Ranks.Five, Suits.SPADE));
        assertEquals(false, HandRankings.hasFlush(cards));

    }

    @org.junit.jupiter.api.Test
    void hasStraightFlush() {
        List<Card> cards4 = Arrays.asList(
                new Card(Ranks.Two, Suits.DIAMOND),
                new Card(Ranks.Four, Suits.DIAMOND),
                new Card(Ranks.Ace, Suits.DIAMOND),
                new Card(Ranks.Six, Suits.DIAMOND),
                new Card(Ranks.Ten, Suits.DIAMOND),
                new Card(Ranks.Three, Suits.CLUB),
                new Card(Ranks.Five, Suits.SPADE));

        assertEquals(true, HandRankings.hasStraightFlush(cards4));

        List<Card> cards3 = Arrays.asList(
                new Card(Ranks.Two, Suits.DIAMOND),
                new Card(Ranks.Four, Suits.DIAMOND),
                new Card(Ranks.Ace, Suits.DIAMOND),
                new Card(Ranks.Six, Suits.DIAMOND),
                new Card(Ranks.Ten, Suits.DIAMOND),
                new Card(Ranks.Ten, Suits.CLUB),
                new Card(Ranks.Five, Suits.SPADE));

        assertEquals(false, HandRankings.hasStraightFlush(cards3));

        List<Card> cards2 = Arrays.asList(
                new Card(Ranks.Two, Suits.DIAMOND),
                new Card(Ranks.Four, Suits.DIAMOND),
                new Card(Ranks.Ace, Suits.CLUB),
                new Card(Ranks.Six, Suits.DIAMOND),
                new Card(Ranks.Three, Suits.DIAMOND),
                new Card(Ranks.Ten, Suits.CLUB),
                new Card(Ranks.Five, Suits.SPADE));

        assertEquals(false, HandRankings.hasStraightFlush(cards2));

    }

    @org.junit.jupiter.api.Test
    void hasRoyalFlush() {
        List<Card> cards2 = Arrays.asList(
                new Card(Ranks.Ten, Suits.DIAMOND),
                new Card(Ranks.Four, Suits.DIAMOND),
                new Card(Ranks.Jack, Suits.CLUB),
                new Card(Ranks.King, Suits.DIAMOND),
                new Card(Ranks.Three, Suits.DIAMOND),
                new Card(Ranks.Queen, Suits.CLUB),
                new Card(Ranks.Ace, Suits.SPADE));

        assertEquals(true, HandRankings.hasRoyalFlush(cards2));

        List<Card> cards3 = Arrays.asList(
                new Card(Ranks.Ten, Suits.DIAMOND),
                new Card(Ranks.Four, Suits.DIAMOND),
                new Card(Ranks.Jack, Suits.CLUB),
                new Card(Ranks.Two, Suits.DIAMOND),
                new Card(Ranks.Three, Suits.DIAMOND),
                new Card(Ranks.Queen, Suits.CLUB),
                new Card(Ranks.Ace, Suits.SPADE));

        assertEquals(false, HandRankings.hasRoyalFlush(cards3));
    }

    @org.junit.jupiter.api.Test
    void getSortedCards() {
        List<Card> cards3 = Arrays.asList(
                new Card(Ranks.Ten, Suits.DIAMOND),
                new Card(Ranks.Four, Suits.DIAMOND),
                new Card(Ranks.Jack, Suits.CLUB),
                new Card(Ranks.Two, Suits.DIAMOND),
                new Card(Ranks.Three, Suits.DIAMOND),
                new Card(Ranks.Queen, Suits.CLUB),
                new Card(Ranks.Ace, Suits.SPADE));

        List<Card> sorted = HandRankings.getSortedCards(cards3);
        assertEquals(true, sorted.get(3).getRank().value < sorted.get(5).getRank().value);
        assertEquals(false, sorted.get(3).getRank().value < sorted.get(2).getRank().value);
        assertEquals(true, sorted.get(6).getRank().value > sorted.get(5).getRank().value);
        assertEquals(false, sorted.get(3).getRank().value > sorted.get(5).getRank().value);
    }
}