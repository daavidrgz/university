package es.udc.redes.webserver;

import java.net.*;
import java.io.*;
import java.util.Properties;

/**
 * Class responsible for receiving the HTTP request and creating a new 'HTTPResponse' object which will handle
 * that request.
 * @author 557
 */

public class ServerThread extends Thread {
    /** Socket in which we are going to receive data.
     */
    private final Socket socket;
    /** Different server properties specified in 'server.properties' such as the default file or
     * the default directory.
     */
    private final Properties properties;
    /**Mutex to control the writes in the 'accessLog.log'.
     */
    private final Object accessMutex;
    /**Mutex to control the writes in the 'errorLog.log'.
     */
    private final Object errorMutex;

    /**
     * @param s Socket in which we are going to receive data.
     * @param properties Different server properties specified in 'server.properties' such as the default file or
     * the default directory.
     * @param accessMutex Mutex to control the writes in the 'accessLog.log'.
     * @param errorMutex Mutex to control the writes in the 'errorLog.log'.
     */

    public ServerThread(Socket s, Properties properties, Object accessMutex, Object errorMutex) {
        this.socket = s;
        this.properties = properties;
        this.accessMutex = accessMutex;
        this.errorMutex = errorMutex;
    }

    /**
     * Method launched when a thread starts; it uses a BufferedReader to read all the request data, analyzing at the
     * same time if an 'If-Modified-Since' header is present. The request received is printed in the stdout
     * and the 'HTTPResponse' response method is called.
     */

    public void run() {
        try {
            BufferedReader sInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            HTTPResponse serverResponse = new HTTPResponse(socket, properties, accessMutex, errorMutex, this.getId());
            StringBuilder allRequest = new StringBuilder();
            String str;
            String ifModSince = null;

            String request = sInput.readLine();
            while ( (str = sInput.readLine()) != null && !str.equals("") ) {
                if ( str.startsWith("If-Modified-Since:") )
                    ifModSince = str.substring(19);
                allRequest.append(str).append("\n");
            }

            System.out.println(request + "\n" + allRequest); // Print all the info received

            serverResponse.response(request, ifModSince);
            sInput.close();

        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();

        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
