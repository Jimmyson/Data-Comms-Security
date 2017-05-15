package com.jimmyson.assignment_2;

import java.net.*;

/**
 * Created by Jimmyson on 3/05/2017.
 */

class UDPcall extends Thread {
    private int Port;
    private boolean Active;
    private DatagramSocket Socket;
    //private InetAddress address = InetAddress.getLocalHost();
    private byte[] sendData;

    UDPcall(int port) throws Exception {
        Port = port;
        Socket = new DatagramSocket(Port);
        Active = true;
        this.start();
    }

    public void run() {
        byte[] incomingData = new byte[1024];
        DatagramPacket incoming = new DatagramPacket(incomingData, incomingData.length);

        try{
            while (Active) {
                Socket.receive(incoming);

                switch(incoming.toString()) {
                    case "ONLINE":
                        Send("I am here", incoming.getAddress());
                        break;
                    case "WHOHAS":
                        Send("I have it!", incoming.getAddress());
                        break;
                    default:
                        break;
                }
            }
        } catch(Exception e) {
            System.out.print("Error receiving data");
        }
    }

    void AllSend(String message) throws Exception{
        MulticastSocket multi = new MulticastSocket();

        sendData = message.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, multi.getInetAddress(), this.Port);
        multi.send(sendPacket);
        multi.close();
    }

    private void Send(String message, InetAddress dest) throws Exception {
        //DatagramSocket clientSocket = new DatagramSocket();

        sendData = message.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, dest, Port);
        Socket.send(sendPacket);

        Socket.close();
    }
}
