package com.jimmyson.assignment_2.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;

/**
 * Created by Jimmyson on 17/05/2017.
 */
public class server {
    private static final int Port = 4000;
    private static ArrayList<Client> Clients = new ArrayList<>();
    private static ArrayList<SimpleEntry<String, ArrayList<InetAddress>>> Files = new ArrayList<>();
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

        byte[] GetIP() {
            return IP;
        }

        String GetName() {
            return Name;
        }

        int GetPort() {
            return Port;
        }
    }

    public static void main(String[] argv) throws Exception {
        Socket = new DatagramSocket(Port);
        int index;
        StringBuilder result;
        byte[] sendData, receiveData = new byte[1024];

        while(Active) {
            byte[] incomingData = new byte[1024];
            DatagramPacket incoming = new DatagramPacket(incomingData, incomingData.length);

            try{
                while (Active) {
                    Socket.receive(incoming);
                    String[] command = CommandSplit(incoming.toString());

                    switch(command[0].toUpperCase()) {

                        case "WELCOME":
                            Clients.add(new Client(incoming.getAddress().getHostName(), incoming.getAddress().getAddress(), 4001));
                            AllSend(incoming.getAddress().getHostName() + " HAS CONNECTED");
                            System.out.println(incoming.getAddress().getHostName()+" AS CONNECTED");
                            break;
                        case "ONLINE":
                            result = new StringBuilder();
                            for (Client c : Clients) {
                                result.append(c.GetName());
                                result.append(" => ");
                                result.append(IPtoString(c.GetIP()));
                                result.append(":");
                                result.append(c.GetPort());
                                result.append("\n");
                            }
                            Send(result.toString(), incoming.getAddress());
                            break;
                        case "ADD":
                            boolean found = false;
                            index = 0;
                            for (SimpleEntry<String, ArrayList<InetAddress>> file : Files) {
                                if (file.getKey().equals(command[1])) {
                                    found = true;
                                    ArrayList<InetAddress> clients = file.getValue();
                                    if (!clients.contains(incoming.getAddress())) {
                                        clients.add(incoming.getAddress());
                                        Files.set(index, new SimpleEntry<>(file.getKey(), clients));
                                    }                                }
                                index++;
                            }
                            if(!found) {
                                ArrayList<InetAddress> newClients = new ArrayList<>();
                                newClients.add(incoming.getAddress());
                                Files.add(new SimpleEntry<>(command[1], newClients));
                            }

                            System.out.println("Added "+command[1]+" from "+incoming.getAddress().getHostAddress());
                            break;
                        case "DELETE":
                            index = 0;
                            for (SimpleEntry<String, ArrayList<InetAddress>> file : Files) {
                                if (file.getKey().equals(command[1])) {
                                    ArrayList<InetAddress> clients = file.getValue();
                                    clients.remove(incoming.getAddress());
                                    if(clients.size() > 0) {
                                        Files.set(index, new SimpleEntry<>(file.getKey(), clients));
                                    } else {
                                        Files.remove(index);
                                    }
                                    break;
                                }
                                index++;
                            }
                            break;
                        case "WHOHAS":
                            result = new StringBuilder();
                            for (SimpleEntry<String, ArrayList<InetAddress>> file : Files) {
                                if (file.getKey().equals(command[1])) {
                                    for (InetAddress IP : file.getValue()) {
                                        result.append(IP.getHostAddress());
                                        result.append("\n");
                                    }
                                    break;
                                }
                            }
                            Send(result.toString(), incoming.getAddress());
                            break;
                        case "BYE":
                            for(Client c : Clients) {
                                if (incoming.getAddress().getAddress() == c.GetIP()) {
                                    AllSend(c.GetName() + " AS DISCONNECTED");
                                    System.out.println(c.GetName() + " AS DISCONNECTED");
                                    Clients.remove(c);
                                    break;
                                }
                            }
                            System.out.print("DEVICE AS DISCONNECTED");
                            break;
                        default:
                            AllSend(incoming.toString());
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

    private static String IPtoString(byte[] IP) {
        if (IP.length == 4) {
            StringBuilder s = new StringBuilder();
            for (byte b : IP) {
                s.append(b & 0xFF);
            }
            return s.toString();
        }
        return null;
    }

    private static String[] CommandSplit(String command) {
        return command.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
    }

    private static void AllSend(String message) throws Exception{
        MulticastSocket multi = new MulticastSocket();

        sendData = message.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, multi.getInetAddress(), Port);
        multi.send(sendPacket);
        multi.close();
    }

    private static void Send(String message, InetAddress dest) throws Exception {
        sendData = message.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, dest, Port);
        Socket.send(sendPacket);

        Socket.close();
    }
}
