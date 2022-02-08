package es.udc.redes.tutorial.tcp.server;

import java.net.*;
import java.io.*;

/**
 * MonoThread TCP echo server.
 */
public class MonoThreadTcpServer {

    public static void main(String argv[]) {
        if ( argv.length != 1 ) { 
            System.err.println("Format: es.udc.redes.tutorial.tcp.server.MonoThreadTcpServer <port>");
            System.exit(-1);
        }

        ServerSocket socket = null;

        try {
            // Create a server socket
            int port = Integer.parseInt(argv[0]);
            socket = new ServerSocket(port);
            socket.setSoTimeout(300000); // Set a timeout of 300 secs

            while ( true ) {
                // Wait for connections
                Socket nSocket = socket.accept();
                System.out.println("SERVER: Connection established with "
                        + nSocket.getInetAddress().toString() + " port " + nSocket.getPort());

                // Set the input channel
                BufferedReader sInput = new BufferedReader(new InputStreamReader(nSocket.getInputStream()));
                // Set the output channel
                PrintWriter sOutput = new PrintWriter(nSocket.getOutputStream(), true);

                // Receive the client message
                String received = sInput.readLine();
                System.out.println("SERVER: Received " + received
                        + " from " + nSocket.getInetAddress().toString() + ":"
                        + nSocket.getPort());

                // Send response to the client
                System.out.println("SERVER: Sending " + received
                        + " to " + nSocket.getInetAddress().toString() + ":"
                        + nSocket.getPort());
                sOutput.println(received);

                // Close the streams
                sOutput.close();
                sInput.close();
                nSocket.close();
            }

        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if ( socket != null )
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
