package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Server {

    private List<Connection> connections = Collections.synchronizedList(new ArrayList<Connection>());
    private ServerSocket server;

    public Server() {
        try {
            server = new ServerSocket(Const.Port);

            while (true) {
                Socket socket = server.accept(); //
                Connection con = new Connection(socket);
                connections.add(con);
                con.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
    }

    private void closeAll() {
        try {
            server.close();

            synchronized (connections) {
                for (Connection c : connections) {
                    c.close();
                }
            }
        } catch (Exception e) {
            System.err.println("An error!");
        }
    }

    private class Connection extends Thread {
        private BufferedReader in;
        private PrintWriter out;
        private Socket socket;

        private String name = "";


        public Connection(Socket socket) {
            this.socket = socket; // repeat this

            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

            } catch (IOException e) {
                e.printStackTrace();
                close();
            }
        }

        @Override
        public void run() {
            try {

                // create queu from messages. we have to store in this queuе no more than 10 messages
                // when new user has joined we send theese messages to him before the dialog has started.
                // the size of queue is 10 messages/ i'll read line and when my queue has 10 messages, I'll delete the first message
                // from the queue. When someone cames now I'll send him the last 10 messages from chat. The all queue.


                name = in.readLine();

                synchronized (connections) {
                    for (Connection c : connections) {
                        c.out.println(name + " has came now");
                    }
                }
                String str;
                while (true) {
                    str = in.readLine();
                    if (str.equals("exit")) break;

                    synchronized (connections) {
                        for (Connection c : connections) {
                            c.out.println(name + " " + str);
                        }
                    }
                }
                synchronized (connections) {
                    for (Connection c : connections) {
                        c.out.println(name + " has left");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                close();
            }
        }

        public void close() {
            try {
                in.close();
                out.close();
                socket.close();

                connections.remove(this);
                if (connections.size() == 0) {
                    Server.this.closeAll();
                    System.exit(0);
                }
            } catch (Exception e) {
                System.err.println("Ups!");
            }
        }
    }
}
