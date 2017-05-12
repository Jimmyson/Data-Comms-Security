package com.jimmyson.assignment_2;

import java.net.*;

/**
 * Created by Jimmyson on 3/05/2017.
 */

class UDPcall {
    private int Port;
    private InetAddress address = InetAddress.getLocalHost();
    private byte[] sendData;

    UDPcall(int port) throws Exception {
        Port = port;
    }

    void AllSend() throws Exception{
        MulticastSocket multi = new MulticastSocket(Port);

        sendData = (address.getHostAddress() + "has connected").getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, multi.getInetAddress(), this.Port);
        multi.send(sendPacket);
        multi.close();
    }

    void Send(String message, InetAddress dest) throws Exception {
        DatagramSocket clientSocket = new DatagramSocket();

        sendData = message.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, dest, Port);
        clientSocket.send(sendPacket);

        clientSocket.close();
    }
}
