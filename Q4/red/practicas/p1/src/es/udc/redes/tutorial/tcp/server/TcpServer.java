package es.udc.redes.tutorial.tcp.server;
import java.io.IOException;
import java.net.*;

/** Multithread TCP echo server. */

public class TcpServer {

    public static void main(String argv[]) {
        if ( argv.length != 1 ) {
            System.err.println("Format: es.udc.redes.tutorial.tcp.server.TcpServer <port>");
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

                // Create a ServerThread object, with the new connection as parameter
                ServerThread sThread = new ServerThread(nSocket);
                // Initiate thread using the start() method
                sThread.start();
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
