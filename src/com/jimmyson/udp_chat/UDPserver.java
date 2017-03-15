//package com.jimmyson.udp_chat;

//import java.io.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Arrays;

/**
 * Created by Jimmyson on 8/03/2017.
 */
public class UDPserver {
    public static void main(String[] args) throws Exception {
        int lPort = 0;

        for (int i = 0; i<args.length; i++) {
            if(args[i].equals("-p") || args[i].equals("--port")) {
                lPort = Integer.parseInt(args[i+1]);
            }
        }

        //SET PORT NUMBER
        if(lPort == 0) {
            //SET PORT INSIDE APPLICATION
            System.out.print("SPECIFY APPLICATION PORT (9876): ");
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            String input = inFromUser.readLine();
            if(!(input.trim().equals(""))) {
                try {
                    lPort = Integer.parseInt(input.trim());
                    System.out.println("SET APPLICATION PORT TO "+lPort);
                } catch (NumberFormatException e) {
                    lPort = 9876;
                    System.out.println("NOT A VALID INPUT. SETTING TO DEFAULT PORT");
                }
            } else {
                //GOT AN EMPTY RESPONSE
                lPort = 9876;
                System.out.println("USING DEFAULT PORT");
            }
        }

        DatagramSocket serverSocket = new DatagramSocket(lPort); //TODO: TRY A RECURSIVE BIND OPERATION

        InetAddress srvAddr = InetAddress.getLocalHost();
        System.out.println("SERVER ACTIVE ON \""+srvAddr+":"+lPort+"\"");
        byte[] receiveData, sendData;
        receiveData = new byte[1024];
        sendData = new byte[1024];

        while(true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);

            String line = new String(receivePacket.getData());
            System.out.println("Received: " + line);

            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();

            String capLine = line.toUpperCase();
            sendData = capLine.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
        }
    }
}
