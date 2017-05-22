package com.jimmyson.assignment_2.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Jimmyson on 12/05/2017.
 * @author James Crawford 9962522
 */
class TCPlistener extends Thread {
    private int Port;
    private ServerSocket Server;
    private ArrayList<TCPsend> Connects = new ArrayList<>();
    private boolean Accept = true;

    /**
     * Initiates a TCP for incoming connections
     *
     * @param port TCP Listing Port
     * @throws Exception When port is used
     */
    TCPlistener(int port) throws Exception {
        Server = new ServerSocket(port);
        Port = port;
        this.start();
    }

    /**
     * Thread process to active the Listener
     * Waits for a connection from a VideoPeer, and begins the request for their file.
     * Threads are send to an array to allow multiple process to be tracked.
     */
    public void run() {
        while (!Server.isClosed() || Accept) { //FIX THIS LOOP
            try {
                Socket request = Server.accept();
                CheckConnections();
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(request.getInputStream()));

                String fileReq = inFromServer.readLine();
                TCPsend t = new TCPsend(request, fileReq);
                Connects.add(t);
                t.start();
            }
            catch (IOException e) {
                //e.printStackTrace();
            }
        }
    }

    /**
     * Kills the Listener
     * @throws Exception
     */
    void Terminate() throws Exception {
        Server.close();
        Accept = false;
        this.interrupt();
    }

    /**
     * Checks the active connections listed to see if they are still active
     *
     * @return true when there is an active connection present
     */
    boolean CheckConnections() {
        for (TCPsend connect : Connects) {
            if(!connect.isClosed()) {
                return true;
            }
            Connects.remove(connect);
        }
        return false;
    }

    /**
     * States if the service is still active
     * @return Boolean state of the service
     */
    boolean Running() { return Accept; }

    /**
     * Returns the TCP Listener Port Number
     * @return Integer of the Port
     */
    int Port() { return Port; }
}
