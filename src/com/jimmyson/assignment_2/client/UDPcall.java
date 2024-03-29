package com.jimmyson.assignment_2.client;

import java.net.*;

/**
 * Created by Jimmyson on 3/05/2017.
 * @author James Crawford 9962522
 */

class UDPcall extends Thread {
    private int Port;
    private boolean Active;
    private DatagramSocket Socket;
    private InetAddress Server;

    /**
     * UDP service to send commands to the directory VideoServer.
     *
     * @param port Server's port number
     * @param server Server's IP address
     * @throws Exception When Port is active use byh another process
     */
    UDPcall(int port, InetAddress server) throws Exception {
        Server = server;
        Port = port;
        Active = true;
        this.start();
    }

    /**
     * Listens for incoming UDP data from the VideoServer and prints to the application.
     */
    public void run() {
        try {
            do {
                Socket = new DatagramSocket(Port);
                byte[] incomingData = new byte[1024];
                DatagramPacket incoming = new DatagramPacket(incomingData, incomingData.length);

                Socket.receive(incoming);
                String data = new String(incoming.getData());
                switch(data) {
                    /*case "ONLINE":
                        Send("I am here", Server);
                        break;
                    case "WHOHAS":
                        Send(Socket.getLocalAddress().getHostAddress() + ":" + Socket.getLocalPort(), incoming.getAddress());
                        break;*/
                    default:
                        System.out.println(data);
                        break;
                }
                Socket.close();
            } while (Active);
        } catch(Exception e) {
            System.out.println("Error receiving data");
            e.printStackTrace();
        }
    }

    /**
     * Sends requets to the Server
     *
     * @param message Command to the VideoServer
     * @throws Exception When Socket is broken
     */
    void Send(String message) throws Exception {
        DatagramSocket sockToServer = new DatagramSocket();
        byte[] sendData = new byte[1024];
        sendData = message.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, Server, Port);
        sockToServer.send(sendPacket);
        sockToServer.close();
    }

    /**
     * Kills the UDP listener process
     */
    void Terminate() {
        Active = false;
        Socket.close();
        this.interrupt();
    }

    /**
     * Returns the active state of the UPD process
     * @return Boolean state of the UDP process
     */
    boolean GetActive() {
        return Active;
    }
}
