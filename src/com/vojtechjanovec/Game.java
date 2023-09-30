package com.vojtechjanovec;

import com.vojtechjanovec.communication.Actions;
import com.vojtechjanovec.communication.Options;
import com.vojtechjanovec.communication.Response;
import com.vojtechjanovec.game.*;

import java.util.*;

public class Game {
    private List<Player> players;
    private Dealer dealer;
    private List<Card> cardsOnTable;
    private Map<Player, Integer> currentBids;
    private boolean end;
    private Object objectRecieved;
    int bank = 0;


    public Game(List<Player> players) {
        this.players = players;
        this.dealer = new Dealer();
        this.currentBids = new LinkedHashMap<>();
        players.stream().forEach(p -> {
            currentBids.put(p, 0);
        });
        this.end = false;


    }

    public List<Player> getPlayers() {
        return players;
    }


    public void start() {
        int lastPlayerBlindIndex = 0;
        int smallBlind = 50;
        int bigBlind = 100;
        dealer.dealChips(this);
        while (this.players.size() > 1) {
            gameRound(lastPlayerBlindIndex, smallBlind, bigBlind);

            dealer.newDeck();
            lastPlayerBlindIndex++;
            bank = 0;
            for (Player p : players) {
                if (p.getChip() <= 0) {
                    players.remove(p);
                    Server.getPlayerHandlers().remove(p);
                } else {
                    p.setCurrentAction(Actions.NONE);
                    currentBids.put(p, 0);
                }

            }


        }


    }

    private void pauseGame(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean sendRequest(Player p, int cb, boolean canRaise) {
        // Send chips alert
        Server.getPlayerHandlers().get(p).sendObject("Your chips: " + p.getChip());
        pauseGame(100);
        // SEND REQUEST
        if (cb == 0 || cb <= currentBids.get(p)) {
            // NO NEED TO BID - ALL OPTIONS
            Server.getPlayerHandlers().get(p).sendObject(new Options(new ArrayList<Actions>(Arrays.asList(Actions.CHECK, Actions.FOLD, Actions.RAISE)), 0));

        } else {
            // NEED TO BID - ONLY FOLD OR RAISE
            Server.getPlayerHandlers().get(p).sendObject(new Options(new ArrayList<Actions>(Arrays.asList(Actions.FOLD, Actions.RAISE)), cb - currentBids.get(p)));
        }
        System.out.println("Sending options");

        while (Server.getObjectRecieved() == null) {
            pauseGame(100);
        }
        Object returnedObject = Server.getObjectRecieved();
        System.out.println(returnedObject);
        Server.setObjectRecieved(null);

        if (returnedObject instanceof Response) {
            Actions a = ((Response) returnedObject).getAction();
            switch (a.toString()) {
                case "CHECK":
                    System.out.println("CHECK OPTION");
                    break;
                case "FOLD":
                    System.out.println("FOLD OPTION");
                    p.fold();
                    currentBids.remove(p);
                    break;
                case "RAISE":
                    System.out.println("RAISE OPTION");
                    int highestBid = ((Response) returnedObject).getValue() + currentBids.get(p);
                    if (highestBid >= cb) {
                        if (canRaise) {
                            cb = highestBid;
                        } else {
                            return false;
                        }

                    }

                    if (cb > p.getChip()+currentBids.get(p)) {
                        System.out.println("Nedostatek žetonu");
                        return false;
                    }
                    currentBids.put(p, cb);
                    p.setChip(p.getChip() - cb);
                    bank += cb;
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    private void gameRound(int lastPlayerBlindIndex, int smallBlind, int bigBlind) {
        lastPlayerBlindIndex = lastPlayerBlindIndex % players.size();
        this.dealer.shuffleDeck();

        Random r = new Random();
        this.dealer.dealCards(this);
        // Send cards to player through socket
        this.cardsOnTable = dealer.getDeck().subList(0, 5);
        for (Card c : cardsOnTable) {
            System.out.println(c);
        }
        // Attach 5 cards that will be visible
        // Show 3 of them.
        ShowedCards<Card> showedCards = new ShowedCards<>(this.cardsOnTable.subList(0, 3));
        // Send card to users
        // SEND CHIPS *ALERT* TO ALL PLAYERS
        for (Map.Entry<Player, SocketHandler> entry : Server.getPlayerHandlers().entrySet()) {
            entry.getValue().sendObject("Your chips: " + entry.getKey().getChip());
        }

        // Handle blinds
        handleBlinds(lastPlayerBlindIndex, smallBlind, bigBlind);

        // Send displayed cards to players
        Server.sendObjectToEveryone(showedCards);


        int currentBid = bigBlind;
        pauseGame(100);

        // Send cards to players
        System.out.println("Sending cards");
        players.stream().forEach(p -> {
            Server.getPlayerHandlers().get(p).sendObject(p.getHand());
        });

        startRound(bigBlind, false);
        // SHOW ANOTHER CARD
        Server.sendObjectToEveryone(new ShowedCards<>(this.cardsOnTable.subList(0, 4)));
        if (!end) {
            startRound(0, true);
        }
        pauseGame(100);
        Server.sendObjectToEveryone(new ShowedCards<>(this.cardsOnTable));
        if (!end) {
            startRound(0, true);
        }
        // Final rankings
        ArrayList<Player> winningPlayers = new ArrayList<>();
        if (currentBids.size() == 1) {
            // All players fold
            winningPlayers.add(currentBids.keySet().iterator().next());

        } else {
            HandRankings highestRanking = HandRankings.HIGH_CARD;
            // IN CASE OF MULTIPLE WINNERS - LIST OF PLAYERS WITH THE SAME HAND RANKING AND HIGHEST CARD

            for (Iterator<Player> it = currentBids.keySet().iterator(); it.hasNext(); ) {
                Player p = it.next();
                ArrayList<Card> cardsToCompare = new ArrayList<>();
                cardsToCompare.addAll(Arrays.asList(p.getHand()));
                cardsToCompare.addAll(cardsOnTable);
                // TODO: * DONE COMPARE CARDS - DETERMINATE THE WINNER
                HandRankings hr = HandRankings.getRankHandRanking(cardsToCompare);
                p.setHr(hr);
                if (winningPlayers.size() == 0) {
                    winningPlayers.add(p);
                }
                if (hr.value > highestRanking.value) {
                    highestRanking = hr;
                    winningPlayers = new ArrayList<>();
                    winningPlayers.add(p);
                }
                if (hr.value == highestRanking.value) {
                    // TWO PLAYERS HAVE SAME HANDRANKING
                    Card highestCard = null;
                    // COMPARE PLAYERS CARD VALUES
                    Map<Card, Player> playersToCompare = new LinkedHashMap<>();
                    for (Card c : p.getHand()) {
                        playersToCompare.put(c, p);
                    }
                    for (Card c : winningPlayers.get(0).getHand()) {
                        playersToCompare.put(c, winningPlayers.get(0));
                    }
                    for (Map.Entry<Card, Player> entry : playersToCompare.entrySet()) {
                        Card c = entry.getKey();
                        if (highestCard != null) {
                            // HIGHER CARD
                            if (c.getRank().value > highestCard.getRank().value) {
                                highestCard = c;
                            } else if (c.getRank().value == highestCard.getRank().value) {
                                // SAME CARD VALUES
                                // TODO: TWO WINNERS - SPLIT BANK BETWEEN PLAYERS
                                // MULTIPLE WINNERS
                                winningPlayers.add(p);
                            }
                        } else {
                            // NO HIGHEST CARD
                            highestCard = c;
                        }
                    }


                }
            }
        }
        if (winningPlayers.size() == 1) {
            Server.sendObjectToEveryone(winningPlayers.get(0).getUserName() + " won this round and recieved " + bank);
            winningPlayers.get(0).setChip(winningPlayers.get(0).getChip()+bank);
        } else {
            String msg = "";
            for (Player p : winningPlayers) {
                msg += p.getUserName() + ", ";
            }
            Server.sendObjectToEveryone(msg + " won this round and recieved " + bank + " in total");
        }

    }

    private void startRound(int currentBid, boolean resetBids) {

        if (resetBids) {
            this.currentBids = new LinkedHashMap<>();
            players.stream().forEach(p -> {
                if (p.getCurrentAction() != Actions.FOLD) {
                    currentBids.put(p, 0);
                }
            });
        }
        // ALL PLAYERS FOLD
        if (currentBids.size() == 1) {
            return;
        }

        int j = 0;
        Set<Integer> values;
        boolean isUnique = false;
        int highestValue = 0;

        for (int i = 0; i < players.size(); i++) {
            Player currentPlayer;
            if (i + 2 >= players.size()) {
                if (j == i + 1) {
                    j = 0;
                } else {
                    j++;
                }
            } else {
                j = i + 2;
            }
            System.out.println(j);
            currentPlayer = players.get(j);

            pauseGame(100);


            if (currentPlayer.getCurrentAction() != Actions.FOLD) {

                if (sendRequest(currentPlayer, currentBid, true)) {
                    for (int k = 0; k < 3; k++) {
                        break;
                    }
                }
            }
            for (Map.Entry<Player, Integer> entry : currentBids.entrySet()) {
                System.out.println(entry.getValue());
                if (entry.getValue() > currentBid) {
                    currentBid = entry.getValue();
                }
            }
        }
        values = new HashSet<Integer>(currentBids.values());
        isUnique = values.size() == 1;
        System.out.println(isUnique);
        for (Map.Entry<Player, Integer> entry : currentBids.entrySet()) {
            System.out.println(entry.getValue());
            if (entry.getValue() > highestValue) {
                highestValue = entry.getValue();
            }
        }
        // Handle bets during round -> all players need to bet the same amount of chips.
        while (!isUnique) {
            for (int i = 0; i < currentBids.size(); i++) {
                if (i + 2 >= players.size()) {
                    if (j == i + 1) {
                        j = 0;
                    } else {
                        j++;
                    }
                } else {
                    j = i + 2;
                }

                Player currentPlayer = players.get(j);
                if (currentBids.containsKey(currentPlayer)) {
                    if (currentBids.get(currentPlayer) != highestValue) {
                        for (int k = 0; k < 3; k++) {
                            if (sendRequest(currentPlayer, highestValue, false)) {
                                break;
                            }
                        }
                    }
                }

            }


            values = new HashSet<Integer>(currentBids.values());
            isUnique = values.size() == 1;
        }

    }

    private void handleSmallBlind(int i, int smallBlind) {
        // BET SMALL BLIND
        if (players.get(i).getChip() >= smallBlind) {
            // HAS ENOUGH CHIPS
            currentBids.put(players.get(i), smallBlind);
            players.get(i).setChip(players.get(i).getChip() - smallBlind);
            Server.sendObjectToEveryone(players.get(i).getUserName() + " vsadil " + smallBlind);

        } else {
            // HAS LESS THAN BLIND - BET ALL / ALL IN
            currentBids.put(players.get(i), players.get(i).getChip());
            Server.sendObjectToEveryone(players.get(i).getUserName() + " vsadil " + players.get(i).getChip() + ", ALL IN");
            players.get(i).setChip(0);

        }
    }

    private void handleBigBlind(int i, int bigBlind) {
        // BET BIG BLIND
        if (players.get(i).getChip() >= bigBlind) {
            // HAS ENOUGH CHIPS
            currentBids.put(players.get(i), bigBlind);
            players.get(i).setChip(players.get(i).getChip() - bigBlind);
            Server.sendObjectToEveryone(players.get(i).getUserName() + " vsadil " + bigBlind);
        } else {
            // HAS LESS THAN BLIND - BET ALL / ALL IN
            currentBids.put(players.get(i), players.get(i).getChip());
            Server.sendObjectToEveryone(players.get(i).getUserName() + " vsadil " + players.get(i).getChip() + ", ALL IN");
            players.get(i).setChip(0);

        }
    }

    private void handleBlinds(int i, int small, int big) {
        pauseGame(100);
        handleBigBlind(i, small);
        pauseGame(100);
        handleSmallBlind(i + 1, big);
        pauseGame(100);
    }

    public Object getObjectRecieved() {
        return objectRecieved;
    }


    public void setObjectRecieved(Object objectRecieved) {
        this.objectRecieved = objectRecieved;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public List<Card> getCardsOnTable() {
        return cardsOnTable;
    }

    public Map<Player, Integer> getCurrentBids() {
        return currentBids;
    }
}



