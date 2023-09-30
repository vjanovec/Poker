package com.vojtechjanovec;

import com.vojtechjanovec.communication.Response;
import com.vojtechjanovec.game.Player;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;

public class SocketHandler implements Runnable {

    Socket sock = null;
    ObjectInputStream in = null;
    Player player = null;


    public SocketHandler(Socket sock) {
        this.sock = sock;
    }

    @Override
    public void run() {
        try {
            try {
                System.out.println("Accepted connection : " + sock);

                // ASK FOR NAME
//                InputStream is = sock.getInputStream();
//                in = new ObjectInputStream(is);
//                Object objectRecieved = (Object) in.readObject();
                Object objectRecieved = recieveObject();
                String userName = objectRecieved.toString();
                if(!Server.getSockets().contains(userName)) {
                    Server.getSockets().add(userName);
                    System.out.println(objectRecieved.toString());
                    // CREATE NEW PLAYER
                    this.player = new Player(objectRecieved.toString());
                    // PUT INTO THE MAP
                        Server.getPlayerHandlers().put(this.player, this);

                    Server.getPlayers().add(player);
                    // SEND ALERT TO ALL PLAYERS
                    new Thread(() -> {
                        Server.sendObjectToEveryone(this.player.getUserName() + " joined the game");
                    }).start();

                } else {
                    Server.getPlayers().stream().forEach(p -> {
                        if(p.getUserName().equals(userName)) {
                            this.player = p;
                        }
                    });
                    synchronized (Server.getPlayerHandlers().get(player)) {
                        Server.getPlayerHandlers().get(player).notify();
                    }
                    Server.getPlayerHandlers().put(player, this);
                    Object o = recieveObject();
                    Server.setObjectRecieved(o);
                }

//                InputStream is = sock.getInputStream();
//                in = new ObjectInputStream(is);
//
//                // Method for deserialization of object
//                Object objectRecieved = (Object) in.readObject();
//                System.out.println(objectRecieved.toString());
//
//                // Object has been deserialized
//                System.out.println("Done.");
                synchronized (this) {
                    this.wait();
                }
//                Thread.sleep(100000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
//                Server.getPlayerHandlers().remove(this.player);
                if (sock != null) sock.close();
                if (in != null) in.close();
//                System.out.println("Socket closed");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendObject(Object msg) {
        ObjectOutputStream oos = null;
        try {
            System.out.println("Sending object");
            oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(msg);
            oos.flush();
        } catch (IOException e) {
            System.out.println("Socket closed");
//            e.printStackTrace();
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

    public Object recieveObject() {
        try {
            ObjectInputStream in = null;
            InputStream is = null;
            is = sock.getInputStream();
            in = new ObjectInputStream(is);

            // Method for deserialization of object
            Object objectRecieved = (Object) in.readObject();
            return objectRecieved;
        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
            System.out.println("Socket closed - recieve object");
        }
        return null;
    }


    public void terminateConnection() {
        try {
            Server.getPlayerHandlers().remove(this.player);
            if (sock != null) sock.close();
            if (in != null) in.close();
            System.out.println("Socket closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
