package com.vojtechjanovec;

import com.vojtechjanovec.game.Player;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class User {
    private Player player;
    public final static int SOCKET_PORT = 8086;      // port
    public final static String SERVER = "127.0.0.1";  // localhost
    public final static int FILE_SIZE = 6022386; // file size temporary hard coded

    static Socket sock = null;
    static ObjectOutputStream oos = null;
    static ObjectInputStream in = null;
    static String playerName = "vjanovec";


    public static void main(String[] args) throws IOException {

        try {
//
            sock = new Socket(SERVER, SOCKET_PORT);
            System.out.println("Connecting...");
            // SEND PLAYER NAME
            oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(playerName);
            oos.flush();

////            while (true) {
                // WAIT FOR INPUT
//                InputStream is = sock.getInputStream();
//                in = new ObjectInputStream(is);
//
//                // Method for deserialization of object
//                Object objectRecieved = (Object) in.readObject();
//                System.out.println(objectRecieved.toString());
////            }



//            new Thread(() -> {
//                System.out.println(waitForObject().toString());
//            }).start();
//            while (true) {
                int bytesRead;
                int current = 0;
                FileOutputStream fos = null;
                BufferedOutputStream bos = null;

                byte[] mybytearray = new byte[FILE_SIZE];
                InputStream is = sock.getInputStream();

                bytesRead = is.read(mybytearray, 0, mybytearray.length);
                current = bytesRead;

                do {
                    bytesRead =
                            is.read(mybytearray, current, (mybytearray.length - current));
                    if (bytesRead >= 0) current += bytesRead;
                } while (bytesRead > -1);

                ByteArrayInputStream byt = new ByteArrayInputStream(mybytearray);
                in = new ObjectInputStream(byt);
                Object objectRecieved = (Object) in.readObject();
                System.out.println(objectRecieved.toString());


//            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
//            if (sock != null) sock.close();
//            if (oos != null) oos.close();
//            if (in != null) in.close();
        }
    }

    public static Object waitForObject() {
        try {
            ObjectInputStream in = null;
            InputStream is = null;
            is = sock.getInputStream();
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
}
