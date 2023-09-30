package com.vojtechjanovec.testclients;

import com.vojtechjanovec.communication.Actions;
import com.vojtechjanovec.communication.Options;
import com.vojtechjanovec.communication.Response;
import com.vojtechjanovec.game.Card;
import com.vojtechjanovec.game.ShowedCards;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

class Client2 {
    private Socket socket;
    private boolean tryToReconnect = true;
    private final Thread heartbeatThread;
    private long heartbeatDelayMillis = 0;

    static ObjectOutputStream oos = null;
    //    static ObjectInputStream in = null;
    static String playerName = "SecondPlayer";
    static Object objectToSend = null;

    public static void main(String[] args) {
        Client2 c1 = new Client2("127.0.0.1", 8086);
    }
    public Client2(final String server, final int port) {
        connect(server, port);
        heartbeatThread = new Thread() {
            public void run() {
//                try {
//                    oos = new ObjectOutputStream(socket.getOutputStream());
//                    oos.writeObject(playerName);
//                    oos.flush();
//                } catch (IOException e) {
//                    System.out.println("Server is offline");
//                    connect(server, port);
//                }
                while (tryToReconnect) {
                    //send a test signal
                    connect(server, port);
                    System.out.println("Reconnecting");
                    try {
                        oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(playerName);
                        oos.flush();


                        if(objectToSend != null) {
                            oos = new ObjectOutputStream(socket.getOutputStream());
                            oos.writeObject(objectToSend);
                            oos.flush();
                            objectToSend = null;
                        }

                        Object objectRecieved = recieveObject();
                        switch(objectRecieved.getClass().getName()){
                            case "java.lang.String":
                                System.out.println(objectRecieved.toString());
                                break;
                            case "com.vojtechjanovec.communication.Options":
                                Options options = (Options) objectRecieved;
                                while(true) {
                                    for (int i = 0; i < options.getOptions().size(); i++) {
                                        System.out.println(String.format("%d) %s - %d", i + 1, options.getOptions().get(i).toString(), options.getValue()));
                                    }
                                    Scanner sc = new Scanner(System.in);
                                    System.out.print("Vyber moznost: ");
                                    int o = sc.nextInt();
                                    int bid = 0;
                                    if(o >=1 && o <= options.getOptions().size()) {
                                        Actions a = options.getOptions().get(o - 1);
                                        int value = options.getValue();
                                        if(a.toString().equals(Actions.RAISE.toString())) {
                                            // ASK FOR VALUE


                                            System.out.println("Zadej hodnotu: ");
                                            bid = sc.nextInt();

                                            while(bid < value) {
                                                System.out.println("Zadejte vyšší hodnotu!");
                                                bid = sc.nextInt();
                                            }

                                        }
                                        System.out.println(a);
                                        Response r = new Response(a, bid);
                                        objectToSend = r;
                                        break;
                                    }
                                }
                                break;
                            case "[Lcom.vojtechjanovec.game.Card;":
                                // Show cards
                                System.out.println("Tvoje karty: ");
                                for(Card c :  (Card[]) objectRecieved) {
                                    System.out.println(c);
                                }
                                break;
                            case "com.vojtechjanovec.game.ShowedCards":
                                System.out.println("Vyložené karty: ");
                                for(Object o : (ShowedCards) objectRecieved) {
                                    System.out.println(o);
                                }

                        }


//                        System.out.println(recieveObject().toString());
//                        if (socket != null) socket.close();
//                        sleep(heartbeatDelayMillis);
                    } catch (IOException e) {
                        // You may or may not want to stop the thread here
                        tryToReconnect = false;
                        System.out.println("Server is offline");

                    }
                }
            }

            ;
        };
        heartbeatThread.start();
    }

    private void connect(String server, int port) {
        try {
            socket = new Socket(server, port);
        } catch (UnknownHostException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private Object recieveObject() {
        try {
            ObjectInputStream in = null;
            InputStream is = null;
            is = socket.getInputStream();
            in = new ObjectInputStream(is);

            // Method for deserialization of object
            Object objectRecieved = (Object) in.readObject();
            String action = objectRecieved.getClass().getName();
            return objectRecieved;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendObject(Object msg) {
        ObjectOutputStream oos = null;
        try {
            System.out.println("Sending object");
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(msg);
            oos.flush();
        } catch (IOException e) {
            System.out.println("Socket closed");
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void shutdown() {
        tryToReconnect = false;
    }
}
