package com.jimmyson.assignment_2;

import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * Created by Jimmyson on 12/05/2017.
 */
public class TCPlistener {
    private ServerSocket Server;
    private ArrayList<TCPsend> Connects = new ArrayList<>();
    private boolean Accept = true;

    TCPlistener(int port) throws Exception{
        Server = new ServerSocket(port);

        while (!Server.isClosed() || Accept) { //FIX THIS LOOP
            TCPsend t = new TCPsend(Server.accept(), "HelloWorld.txt");
            Connects.add(t);
            t.start();
        }
    }

    public void Terminate() throws Exception {
        Accept = false;
        Server.close();
    }

    boolean CheckConnections() {
        for (TCPsend connect : Connects) {
            if(connect.isConnected()) {
                return true;
            }
        }
        return false;
    }

    public boolean Running() { return Accept; }
}
