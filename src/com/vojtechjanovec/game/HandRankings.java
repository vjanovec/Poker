package com.vojtechjanovec.game;

import java.util.*;


import java.util.stream.Collectors;

public enum HandRankings {
    HIGH_CARD(1), // * DONE
    PAIR(2), // * DONE
    TWO_PAIR(3), // * DONE
    THREE_OF_A_KIND(4), // * DONE
    STRAIGHT(5), // postupka DONE
    FLUSH(6), // barva DONE
    FULL_HOUSE(7), // 3 a 2 * DONE
    FOUR_OF_A_KIND(8), // 4 DONE
    STRAIGHT_FLUSH(9), // postupka v barve // DONE
    ROYAL_FLUSH(10); // kralovska postupka // DONE

    public final int value;
    private HandRankings(int value) {
        this.value = value;
    }

    public static HandRankings getRanking(List<Card> cards) {
        List<Ranks> ranks = Arrays.asList(Ranks.values());


        HandRankings currentRank = getRankHandRanking(cards);

        // HANDLE STRAIGHT
        if(hasStraight(cards) && HandRankings.STRAIGHT.value > currentRank.value) {
            currentRank = HandRankings.STRAIGHT;
        }
        // HANDLE FLUSH
        if(hasFlush(cards) && HandRankings.FLUSH.value > currentRank.value) {
            currentRank = HandRankings.FLUSH;
        }
        // HANDLE STRAIGHT_FLUSH
        if(hasStraightFlush(cards) && HandRankings.STRAIGHT_FLUSH.value > currentRank.value) {
            currentRank = HandRankings.STRAIGHT_FLUSH;
        }
        // HANDLE ROYAL_FLUSH
        if(hasRoyalFlush(cards) && HandRankings.ROYAL_FLUSH.value > currentRank.value) {
            currentRank = HandRankings.ROYAL_FLUSH;
        }

        return currentRank;
    }

    public static HandRankings getRankHandRanking(List<Card> cards) {
        int rankStreak = 0;
        List<Ranks> ranks = Arrays.asList(Ranks.values());
        HandRankings rank = HandRankings.HIGH_CARD;
        for(Ranks r : ranks) {
            int s = 0;
            for(Card c : cards) {
                if(c.getRank().equals(r)) {
                    s++;
                }
            }
            if(s == 2 && rankStreak == 2) {
                // TWO_PAIR
                rank = HandRankings.TWO_PAIR;
            }
            if(s >= 2 && rankStreak >= 3 || s >=3 && rankStreak >= 2) {
                rank = HandRankings.FULL_HOUSE;
            }
            if(s > rankStreak) {
                rankStreak = s;
            }
        }
        switch (rankStreak) {
            case 1:
                // SINGLE CARD
                rank = HandRankings.HIGH_CARD;
                break;
            case 2:
                rank = HandRankings.PAIR;
                // PAIR
                break;
            case 3:
                rank = HandRankings.THREE_OF_A_KIND;
                // THREE_OF_A_KIND
                break;
            case 4:
                rank = HandRankings.FOUR_OF_A_KIND;
                // FOUR_OF_A_KIND
                break;
        }
        return rank;
    }

    public static boolean hasStraight(List<Card> cards) {
        List<Card> sortedCards = getSortedCards(cards);
        // STRAIGHT
        int straightStreak = 1;
        for(int i=0; i<sortedCards.size(); i++) {
            if(i != sortedCards.size()-1) {
                if(sortedCards.get(i).getRank().value+1 == sortedCards.get(i+1).getRank().value) {
                    straightStreak++;
                }
            }
        }
        return straightStreak >=5;
    }

    public static boolean hasFlush(List<Card> cards){
        // FLUSH
        int flushStreak = 1;
        for(Suits suit : Suits.values()) {
            int s = 0;
            for(Card c : cards) {
                if(c.getSuit().equals(suit)) {
                    s++;
                }
            }
            if(s > flushStreak) {
                flushStreak = s;
            }
        }
        return flushStreak >= 5;

    }

    public static boolean hasStraightFlush(List<Card> cards) {
        return hasFlush(cards) && hasStraight(cards);
    }

    public static boolean hasRoyalFlush(List<Card> cards) {
        List<Ranks> ranks = cards.stream().map(c -> c.getRank()).collect(Collectors.toList());
        return hasStraight(cards) &&
                ranks.contains(Ranks.Ace) &&
                ranks.contains(Ranks.King) &&
                ranks.contains(Ranks.Queen) &&
                ranks.contains(Ranks.Jack) &&
                ranks.contains(Ranks.Ten);
    }


    public static List<Card> getSortedCards(List<Card> cards) {
        List<Card> sorted = new ArrayList<>(cards);
        Collections.sort(sorted, new Comparator<Card>() {
            @Override
            public int compare(Card o1, Card o2) {

                return o1.getRank().value - o2.getRank().value;
            }
        });
        return sorted;
    }

    public static List<Card> getSortedCards2(List<Card> cards) {

        Card[] cardArr = cards.toArray(new Card[cards.size()]);
        boolean sorted = false;
        Card temp;
        while(!sorted) {
            sorted = true;
            for (int i = 0; i < cardArr.length - 1; i++) {
                if (cardArr[i].getRank().value > cardArr[i+1].getRank().value) {
                    temp = cardArr[i];
                    cardArr[i] = cardArr[i+1];
                    cardArr[i+1] = temp;
                    sorted = false;
                }
            }
        }
        List<Card> sortedCards = Arrays.asList(cardArr);
        return sortedCards;
    }

}
