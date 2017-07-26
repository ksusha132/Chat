package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Server {

    private List<Connection> connections = Collections.synchronizedList(new ArrayList<Connection>());
    private List<Messages> messages = Collections.synchronizedList(new ArrayList<Messages>());
    //    private ArrayDeque<String> messages = new ArrayDeque<String>();
    //    private ArrayDeque<String> names = new ArrayDeque<String>();

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

                name = in.readLine();

                synchronized (connections) {
                    for (Connection c : connections) {
                        c.out.println(name + " has came now");
                    }
                }

                Connection lastClient = connections.get(connections.size() - 1); // got last client
                synchronized (messages) {
                    for (Messages m : messages) {
                        lastClient.out.println(m);
                    }
                }


                String str;
                while (true) {
                    str = in.readLine();
                    synchronized (messages) {
                        Messages ms = new Messages(name, str);
                        messages.add(ms);
                        if (messages.size() == 10) {
                            messages.remove(messages.size() - 10); // delete first element - first message
                        }
                    }
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
