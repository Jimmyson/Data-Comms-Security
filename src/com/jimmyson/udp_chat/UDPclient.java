//package com.jimmyson.udp_chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by Jimmyson on 8/03/2017.
 */
public class UDPclient {

    public static void main(String[] args) throws Exception
    {
        int srvPort = 0;
        String srvAddr = "";
        InetAddress IPAddress = null;
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        for (int i = 0; i<args.length; i++) {
            if(args[i].equals("-p") || args[i].equals("--port")) {
                srvPort = Integer.parseInt(args[i+1]);
            }
            if(args[i].equals("-d") || args[i].equals("--dest")) {
                String[] parts = args[i+1].split("\\.");
                if(parts.length == 4) {
                    byte[] ip = new byte[4];

                    for (int j = 0; j < parts.length; j++) {
                        ip[j] = (byte)Integer.parseInt(parts[j]);
                    }

                    IPAddress = InetAddress.getByAddress(ip);
                } else {
                    IPAddress = InetAddress.getByName(args[i + 1]);
                }
            }
        }

        //SET PORT NUMBER
        if(srvPort == 0) {
            //SET PORT INSIDE APPLICATION
            System.out.print("SPECIFY APPLICATION PORT (9876): ");

            String input = inFromUser.readLine();
            if(!(input.trim().equals(""))) {
                try {
                    srvPort = Integer.parseInt(input.trim());
                    System.out.println("SET APPLICATION PORT TO "+srvPort);
                } catch (NumberFormatException e) {
                    srvPort = 9876;
                    System.out.println("NOT A VALID INPUT. SETTING TO DEFAULT PORT");
                }
            } else {
                //GOT AN EMPTY RESPONSE
                srvPort = 9876;
                System.out.println("USING DEFAULT PORT");
            }
        }
        if (IPAddress == null) {
            //SPECIFY DESTINATION
            System.out.print("SPECIFY DESTINATION DEVICE: ");
            String input = inFromUser.readLine();

            String[] parts = input.split("\\.");
            if(parts.length == 4) {
                byte[] ip = new byte[4];

                for (int j = 0; j < parts.length; j++) {
                    ip[j] = (byte)Integer.parseInt(parts[j]);
                }

                IPAddress = InetAddress.getByAddress(ip);
            } else {
                IPAddress = InetAddress.getByName(input);
            }

            System.out.println("SET ADDRESS TO "+input);
        }

        while(true) {
            DatagramSocket clientSocket = new DatagramSocket();

            byte[] sendData, receiveData;
            sendData = new byte[1024];
            receiveData = new byte[1024];

            String sentence = inFromUser.readLine();
            sendData = sentence.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, srvPort);
            clientSocket.send(sendPacket);

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);

            String modifiedSentence = new String(receivePacket.getData());
            System.out.println("FROM SERVER:" + modifiedSentence);

            clientSocket.close();
        }
    }
}
