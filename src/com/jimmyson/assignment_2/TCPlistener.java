package com.jimmyson.assignment_2;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Jimmyson on 12/05/2017.
 */
class TCPlistener {
    private int Port;
    private ServerSocket Server;
    private ArrayList<TCPsend> Connects = new ArrayList<>();
    private boolean Accept = true;

    TCPlistener(int port) throws Exception {
        Server = new ServerSocket(port);
        Port = port;

        while (!Server.isClosed() || Accept) { //FIX THIS LOOP
            Socket request = Server.accept();
            if(!CheckFile(request.toString())) {
                request.close();
            } else {
                TCPsend t = new TCPsend(request, "HelloWorld.txt");
                Connects.add(t);
                t.start();
            }
        }
    }

    private boolean CheckFile(String filename) {
        File fileReq = new File(filename);
        return fileReq.exists();
    }

    void Terminate() throws Exception {
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

    boolean Running() { return Accept; }
    int Port() { return Port; }
}
