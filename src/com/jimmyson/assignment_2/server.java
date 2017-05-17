package com.jimmyson.assignment_2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;
import java.util.Iterator;

/**
 * Created by Jimmyson on 17/05/2017.
 */
public class server {
    private static final int Port = 4000;
    private static ArrayList<Client> Clients = new ArrayList<>();
    private static ArrayList<SimpleEntry<String, ArrayList<byte[]>>> Files;
    private static boolean Active = true;
    private static byte[] sendData;
    private static DatagramSocket Socket;

    public static class Client {
        private String Name;
        private byte[] IP = new byte[4];
        private int Port;

        Client(String name, byte[] ip, int port) {
            this.Name = name;
            this.IP = ip;
            this.Port = port;
        }

        public byte[] GetIP() {
            return IP;
        }

        public String GetName() {
            return Name;
        }

        public int GetPort() {
            return Port;
        }
    }

    /*public class FileRecord {
        private String Name;
        private ArrayList<Client> Owners = new ArrayList<>();

        FileRecord(String name) {
            this.Name = name;
        }

        public void AddClient(Client client) {
            Owners.add(client);
        }

        public void RemoveClient(Client client) {
            Owners.remove(client);
        }

        public ArrayList<Client> GetOwners() {
            return Owners;
        }
    }*/

    public static void main(String[] argv) throws Exception {
        //READ PORT NUMBER
        Socket = new DatagramSocket(Port);
        byte[] sendData, receiveData = new byte[1024];

        while(Active) {
            byte[] incomingData = new byte[1024];
            DatagramPacket incoming = new DatagramPacket(incomingData, incomingData.length);

            try{
                while (Active) {
                    Socket.receive(incoming);

                    switch(incoming.toString()) {
                        case "ONLINE":
                            //READ STRING FOR IP ADDRESS AND PORT;
                            //LOG DEVICE
                            Clients.add(new Client(incoming.getAddress().getHostName(), incoming.getAddress().getAddress(), incoming.getPort()));
                            AllSend("DEVICE HAS CONNECTED");
                            break;
                        case "ADD":
                            for (SimpleEntry<String, ArrayList<byte[]>> file : Files) {
                                if (file.getKey().equals("FILENAME")) break;
                                else {
                                    ArrayList<byte[]> clients = file.getValue();
                                    //@TODO: FIX THIS!
                                    /*if (!clients.contains(incoming.getAddress().getAddress())) {
                                        file.setValue(clients.add(incoming.getAddress().getAddress()));
                                    }*/
                                }
                                ArrayList<byte[]> newClients = new ArrayList<>();
                                newClients.add(incoming.getAddress().getAddress());
                                Files.add(new SimpleEntry<>("FILENAME", newClients));
                            }
                            break;
                        case "WHOHAS":
                            Send(Socket.getLocalAddress().getHostAddress() + ":" + Socket.getLocalPort(), incoming.getAddress());
                            break;
                        case "BYE":
                            //REMOVE IP ADDRESS + PORT
                            AllSend("DEVICE AS DISCONNECTED");
                        default:
                            System.out.print(incoming.toString());
                            break;
                    }
                }
            } catch(Exception e) {
                System.out.print("Error receiving data");
            }

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            Socket.receive(receivePacket);

            String line = new String(receivePacket.getData());
            System.out.println("Received: " + line);

            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();

            String capLine = line.toUpperCase();
            sendData = capLine.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            Socket.send(sendPacket);

            Active = false;
        }
    }

    private static void AllSend(String message) throws Exception{
        MulticastSocket multi = new MulticastSocket();

        sendData = message.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, multi.getInetAddress(), Port);
        multi.send(sendPacket);
        multi.close();
    }

    //@TODO: Handle at Server
    private static void Send(String message, InetAddress dest) throws Exception {
        //DatagramSocket clientSocket = new DatagramSocket();

        sendData = message.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, dest, Port);
        Socket.send(sendPacket);

        Socket.close();
    }
}
