package com.jimmyson.assignment_2.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;

/**
 * Created by Jimmyson on 17/05/2017.
 * @author James Crawford 9962522
 */
public class VideoServer {
    /**
     * Array of Clients
     * Array of files with a sub array of clients that have said file
     */
    private static int Port;
    private static ArrayList<VideoServer.Client> Clients = new ArrayList<>();
    private static ArrayList<SimpleEntry<String, ArrayList<InetAddress>>> Files = new ArrayList<>();
    private static boolean Active = true;
    private static byte[] sendData;

    /**
     * A VideoPeer object
     */
    static class Client {
        private String Name;
        private byte[] IP = new byte[4];
        private int Port;

        /**
         * Builds the Client object, storing only the essentials
         * @param name Hostname
         * @param ip IP address as a byte array
         * @param port Port of their TCP Listener
         */
        Client(String name, byte[] ip, int port) {
            this.Name = name;
            this.IP = ip;
            this.Port = port;
        }

        /**
         * Return's their IP address
         * @return Client's IPv4
         */
        byte[] GetIP() {
            return IP;
        }

        /**
         * Return's their Hostname
         * @return Client's Hostname
         */
        String GetName() {
            return Name;
        }

        /**
         * Return's their TCP Port address
         * @return Client's TCP Port address
         */
        int GetPort() {
            return Port;
        }
    }

    /**
     * UDP listener for the incoming commands
     */
    protected static class UDPlistener extends Thread {
        private DatagramSocket Socket;

        /**
         * Sets up the host socket at the specified port
         * @param port Server's Port
         * @throws Exception When port is in use.
         */
        UDPlistener(int port) throws Exception{
            Socket = new DatagramSocket(port);
            this.start();
        }

        /**
         * Thread command to initiate the UDP listener
         * Lists for the following commands
         * * WELCOME (Registers a new VideoPeer)
         * * ONLINE (Sends response of active clients)
         * * ADD (Registers a file to the DB, including VideoPeer)
         * * DELETE (Removes a cilent from a file association, and file when no more clients have file)
         * * WHOHAS (Sends response of active clients with file
         * * BYE (Deregisters a VideoPeer)
         */
        public void run() {
            int index;
            StringBuilder result;
            boolean found;

            //DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            try {
                do {
                    byte[] incomingData = new byte[1024];
                    DatagramPacket incoming = new DatagramPacket(incomingData, incomingData.length);

                    Socket.receive(incoming);
                    String data = new String(incoming.getData()).trim();
                    String[] command = CommandSplit(data);

                    switch(command[0].trim().toUpperCase()) {
                        case "WELCOME":
                            found = false;
                            for (VideoServer.Client c : Clients) {
                                if (incoming.getAddress().getHostName().equals(c.GetName())) {
                                    found = true;
                                }
                            }
                            if(!found)
                                Clients.add(new VideoServer.Client(incoming.getAddress().getHostName(), incoming.getAddress().getAddress(), 4001));
                            //AllSend(incoming.getAddress().getHostName() + " HAS CONNECTED");
                            System.out.println(incoming.getAddress().getHostName()+" AS CONNECTED");
                            break;
                        case "ONLINE":
                            result = new StringBuilder();
                            for (VideoServer.Client c : Clients) {
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
                            found = false;
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
                            for (VideoServer.Client c : Clients) {
                                if (incoming.getAddress().getHostName().equals(c.GetName())) {
                                    //AllSend(c.GetName() + " AS DISCONNECTED");
                                    System.out.println(c.GetName() + " AS DISCONNECTED");
                                    Clients.remove(c);

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
                                }
                            }
                            break;
                        default:
                            //AllSend(incoming.toString());
                            System.out.println(data);
                            break;
                    }
                } while (Active);
            } catch(Exception e) {
                System.out.print("Error receiving data");
                e.printStackTrace();
            }
        }
    }

    /**
     * Server's interaction with process
     *
     * @param argv Program Arguments
     * @throws Exception When input invalid
     */
    public static void main(String[] argv) throws Exception {
        int index = 0;
        for (String param : argv) {
            switch (param) {
                case "-p":
                    try {
                        Port = Integer.parseInt(argv[index + 1]);
                    } catch (Exception e) {
                        Port = 4000;
                    }
                break;
            }
            index++;
        }

        if (Port < 1024) {
            Port = 4000;
        }

        UDPlistener listener = new UDPlistener(Port);

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        while(Active) {
            String[] command = CommandSplit(input.readLine());
            switch (command[0].trim().toUpperCase()) {
                case "ONLINE":
                    index = 0;
                    System.out.println("---------------");
                    System.out.println("Users Connected");
                    for (Client c : Clients) {
                        System.out.println("\t" + c.GetName() + " => " + IPtoString(c.GetIP()) + ":" + c.GetPort());
                        index++;
                    }
                    if (index == 0)
                        System.out.println("No users connected");
                    System.out.println("---------------");
                    break;
                case "FILES":
                    System.out.println("---------------");
                    System.out.println("Files logged in System");
                    for (SimpleEntry<String, ArrayList<InetAddress>> file : Files) {
                        System.out.println(file.getKey() + ":");
                        for (InetAddress IP : file.getValue()) {
                            System.out.println("\t" + IP.getHostAddress());
                        }
                    }
                    System.out.println("---------------");
                    break;
                case "HELP":
                    PrintHelp();
                    break;
                case "QUIT":
                case "STOP":
                    System.out.println("TERMINATING...");
                    Active = false;
                    listener.interrupt();
                    Send("SERVER HAS SELF-TERMINATED", InetAddress.getLocalHost());
                    //AllSend("SERVER HAS SELF-TERMINATED");
                    break;
                default:
                    //AllSend("SERVER: "+input.readLine());
                    break;
            }
        }
    }

    /**
     * Print the Commands for the Server
     */
    private static void PrintHelp() {
        System.out.println("---------------");
        System.out.println("Accepted Commands");
        System.out.println();
        System.out.println("ONLINE - Print Connected Users");
        System.out.println("FILES - Print Listed Files");
        System.out.println("HELP - Print this output");
        System.out.println("QUIT/STOP - Terminate Server");
        System.out.println("---------------");
    }

    /**
     * Converts IP address to String to be printed
     *
     * @param IP IP Address array
     * @return IP Address as string
     */
    private static String IPtoString(byte[] IP) {
        if (IP.length == 4) {
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < IP.length; i++) {
                s.append(IP[i] & 0xFF);
                if (i != IP.length-1) {
                   s.append('.');
                }
            }
            return s.toString();
        }
        return null;
    }

    /**
     * Splits the inputs at the spaces for command breakdown.
     * Does not encapsulate Quotation  Marks
     *
     * @param command User Input
     * @return Array of sub-commands
     */
    private static String[] CommandSplit(String command) {
        //return command.split("(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        return command.split(" ");
    }

    /*private static void AllSend(String message) throws Exception{
        MulticastSocket multi = new MulticastSocket();

        sendData = message.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, multi.getInetAddress(), Port);
        multi.send(sendPacket);
        multi.close();
        System.out.println("SERVER: "+message);
    }*/

    /**
     * Sends response to the Client
     *
     * @param message Command to the VideoPeer
     * @param dest Client's IP
     * @throws Exception When Socket is broken
     */
    private static void Send(String message, InetAddress dest) throws Exception {
        byte[] sendData = new byte[1024];
        sendData = message.getBytes();
        DatagramSocket socket = new DatagramSocket();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, dest, Port);
        socket.send(sendPacket);
    }
}
