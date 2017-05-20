package com.jimmyson.assignment_2.client;

import java.net.*;

/**
 * Created by Jimmyson on 3/05/2017.
 */

class UDPcall extends Thread {
    private int Port;
    private boolean Active;
    private DatagramSocket Socket;
    private InetAddress Server;

    UDPcall(int port, InetAddress server) throws Exception {
        Server = server;
        Port = port;
        Socket = new DatagramSocket(Port);
        Active = true;
        this.start();
    }

    //@TODO: Handle at Sever
    public void run() {
        byte[] incomingData = new byte[1024];
        DatagramPacket incoming = new DatagramPacket(incomingData, incomingData.length);

        try{
            while (Active) {
                Socket.receive(incoming);

                switch(incoming.toString()) {
                    /*case "ONLINE":
                        Send("I am here", Server);
                        break;
                    case "WHOHAS":
                        Send(Socket.getLocalAddress().getHostAddress() + ":" + Socket.getLocalPort(), incoming.getAddress());
                        break;*/
                    default:
                        System.out.print(incoming.toString());
                        break;
                }
            }
        } catch(Exception e) {
            System.out.print("Error receiving data");
        }
    }

    void Send(String message) throws Exception {
        byte[] sendData = message.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, Server, Port);
        Socket.send(sendPacket);

        Socket.close();
    }

    void Terminate() {
        Active = false;
    }

    boolean GetActive() {
        return Active;
    }
}
