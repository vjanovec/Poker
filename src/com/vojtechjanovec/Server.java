package com.vojtechjanovec;

import com.vojtechjanovec.game.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    public static final int SOCKET_PORT = 8086;
    public final static String SERVER = "127.0.0.1";
    static ServerSocket servsock = null;
//    private static Collection<SocketHandler> activeSocketHandlers = new ConcurrentLinkedQueue<>();
    private static Map<Player, SocketHandler> playerHandlers = new ConcurrentHashMap();;
    private static List<Player> players = new ArrayList<>();
    private static List<String> sockets = new ArrayList<>();
    private static Game game = null;
    private static Object objectRecieved = null;

    public static void main(String[] args) throws IOException {
        try {
            servsock = new ServerSocket(SOCKET_PORT);
//            new Thread(() -> {
//                while(true) {
//                    sendObjectToEveryone("Testing object");
//
//                    System.out.println("Sending testing object");
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                }).start();

            while(true) {
                System.out.println("Waiting");
                new Thread(new SocketHandler(servsock.accept())).start();
//                System.out.print("\nPlayers: ");
//                sockets.stream().forEach(s -> System.out.print(s+", "));
//                System.out.print("\n");
//                System.out.println("Active sockets");

//                    for (Map.Entry<Player, SocketHandler> entry : Server.getPlayerHandlers().entrySet()) {
//                        System.out.print(entry.getValue() + "\n");
//                    }

                if(players.size() >= 3 && game == null) {
                    game = new Game(players);
                    new Thread(() -> {
                        game.start();
                    }).start();

                }
            }
//            new Game(players).start();
        } finally {
//            servsock.close();
        }
    }
    public static void sendObjectToEveryone(Object o) {
        System.out.println("Current connections "+Server.getPlayerHandlers().size());
        for (Map.Entry<Player, SocketHandler> entry : Server.getPlayerHandlers().entrySet()) {
            entry.getValue().sendObject(o);
        }

    }

//    public static Collection<SocketHandler> getActiveSocketHandlers() {
//        return activeSocketHandlers;
//    }

    public static Map<Player, SocketHandler> getPlayerHandlers() {
        return playerHandlers;
    }

    public static List<Player> getPlayers() {
        return players;
    }

    public static List<String> getSockets() {
        return sockets;
    }

    public static Object getObjectRecieved() {
        return objectRecieved;
    }

    public static void setObjectRecieved(Object objectRecieved) {
        Server.objectRecieved = objectRecieved;
    }
}
