package es.udc.redes.webserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.util.Properties;

/**
 * Class in charge of creating a TCP ServerSocket that receives requests and managing them in different threads
 * @author 557
 */

public class WebServer {
    /**
     * Method that will read the properties file "server.properties" in order to create a ServerSocket listening in the
     * port specified by that file; every time a new connection is established, a new thread
     * is going to be launched manging the request. A timeout of 300 seconds is set by default.
     * @param args Arguments passed to the program; the number of them should be 0
     */

    public static void main(String[] args) {
        if ( args.length > 0 ) {
            System.err.println("No arguments allowed");
            System.exit(-1);
        }
        ServerSocket socket = null;
        Properties properties = new Properties();
        Object accessMutex = new Object();
        Object errorMutex = new Object();

        try {
            try {
                properties.load(new FileInputStream("p1-files/server.properties"));
                //Deleting log files
                File accessLog = new File("p1-files/log/access.log");
                File errorLog = new File("p1-files/log/error.log");
                Files.writeString(accessLog.toPath(), "");
                Files.writeString(errorLog.toPath(), "");
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                System.exit(-1);
            }
            // Create a server socket
            socket = new ServerSocket(Integer.parseInt(properties.getProperty("PORT")));
            socket.setSoTimeout(300000);
            while ( true ) {
                // Wait for connections
                Socket nSocket = socket.accept();
                System.out.println("SERVER: Connection established with "
                        + nSocket.getInetAddress().toString() + " port " + nSocket.getPort());
                // Create a ServerThread object, with the new connection as parameter
                ServerThread sThread = new ServerThread(nSocket, properties, accessMutex, errorMutex);
                sThread.start(); // Initiate thread using the start() method
            }

        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally{
            try {
                if ( socket != null )
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
}
