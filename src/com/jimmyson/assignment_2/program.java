package com.jimmyson.assignment_2;

/**
 * Created by Jimmyson on 3/05/2017.
 */

import java.io.Console;
import java.net.InetSocketAddress;

public class program {
    private static int port;

    public static void main(String argv[]) throws Exception
    {
        int index = 0;
        for (String param : argv) {
            switch (param) {
                case "-p":
                    port = Integer.parseInt(argv[index+1]);
            }
            index++;
        }

        InetSocketAddress socket = new InetSocketAddress(port);

        //Check For Port Number
        //Get IP Address

        //Broadcast Socket Set

        //UDP call for active servers

        //TCP connection for send

        //TCP receive for fetch
    }

    private static boolean shutdownCheck()
    {
        //Check for active connections

        //If Active, ask
            //IF True, reject incoming connections and wait for transfer to end
            //IF False, Nothing

        return false;
    }
}
