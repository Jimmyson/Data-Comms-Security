package com.jimmyson.web_server;

import java.net.*;
import java.io.*;

/**
 * Created by jimmyson on 22/03/17.
 * Code from http://javarevisited.blogspot.com.au/2015/06/how-to-create-http-server-in-java-serversocket-example.html
 */
public class SimpleHTTPServer {

    public static void main(String args[] ) throws IOException {

        ServerSocket server = new ServerSocket(8080);
        System.out.println("Listening for connection on port 8080 ....");
        while (true) {
            Socket clientSocket = server.accept();
            InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            String line = reader.readLine();
            while (!line.isEmpty()) {
                System.out.println(line);
                line = reader.readLine();
            }
        }
    }
}
