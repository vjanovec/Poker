package com.vojtechjanovec.testclients;

import com.vojtechjanovec.Client;

public class ClientStart {
    public static void main(String[] args) {

        Client1 c1 = new Client1("127.0.0.1", 8086);
        Client2 c2 = new Client2("127.0.0.1", 8086);
        Client3 c3 = new Client3("127.0.0.1", 8086);

    }
}
