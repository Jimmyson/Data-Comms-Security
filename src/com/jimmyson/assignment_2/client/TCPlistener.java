package com.jimmyson.assignment_2.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Jimmyson on 12/05/2017.
 */
class TCPlistener extends Thread {
    private int Port;
    private ServerSocket Server;
    private ArrayList<TCPsend> Connects = new ArrayList<>();
    private boolean Accept = true;

    TCPlistener(int port) throws Exception {
        Server = new ServerSocket(port);
        Port = port;
        this.start();
    }

    public void run() {
        while (!Server.isClosed() || Accept) { //FIX THIS LOOP
            try {
                Socket request = Server.accept();
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(request.getInputStream()));

                String fileReq = inFromServer.readLine();
                TCPsend t = new TCPsend(request, fileReq);
                Connects.add(t);
                t.start();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void Terminate() throws Exception {
        Server.close();
        Accept = false;
    }

    boolean CheckConnections() {
        for (TCPsend connect : Connects) {
            if(connect.isConnected()) {
                return true;
            }
        }
        return false;
    }

    boolean Running() { return Accept; }
    int Port() { return Port; }
}
