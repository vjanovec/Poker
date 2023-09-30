package com.vojtechjanovec;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private Socket socket;
    private boolean tryToReconnect = true;
    private final Thread heartbeatThread;
    private long heartbeatDelayMillis = 5000;

    static ObjectOutputStream oos = null;
    //    static ObjectInputStream in = null;
    static String playerName = "vjanovec2";

    public Client(final String server, final int port) {
        connect(server, port);
        heartbeatThread = new Thread() {
            public void run() {
                try {
                    oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(playerName);
                    oos.flush();
                } catch (IOException e) {
                    System.out.println("Server is offline");
                    connect(server, port);
                }
                while (tryToReconnect) {
                    //send a test signal
                    connect(server, port);
                    try {
                        System.out.println(recieveObject().toString());
                        sleep(heartbeatDelayMillis);
                    } catch (InterruptedException e) {
                        // You may or may not want to stop the thread here
                         tryToReconnect = false;
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

    public void shutdown() {
        tryToReconnect = false;
    }
}
