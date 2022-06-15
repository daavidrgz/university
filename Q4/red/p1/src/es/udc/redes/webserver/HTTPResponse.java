package es.udc.redes.webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class analyzes all the different parameters of the HTTP request and sends the correspond message to the client.
 */
public class HTTPResponse {
    /**Root path of the server.*/
    private final String basePath; // Server properties
    /**Default file to be opened if no one is specified in the request.*/
    private final File defaultFile;
    /**Bool to allow the client to see the files inside directories.*/
    private final boolean allow;
    /**Date formatter to send responses.*/
    private final SimpleDateFormat date = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
    /**Date formatter to register requests in the logs.*/
    private final SimpleDateFormat dateLog = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss z");

    // Only opening the files one time
    /**Error 400 html page*/
    private final File error400 = new File("p1-files/error/error400.html");
    /**Error 403 html page*/
    private final File error403 = new File("p1-files/error/error403.html");
    /**Error 404 html page*/
    private final File error404 = new File("p1-files/error/error404.html");
    /**Accesses Log*/
    private final File accessLog = new File("p1-files/log/access.log");
    /**Errors Log*/
    private final File errorLog = new File("p1-files/log/error.log");

    /**Mutex to control the writes in the 'accessLog.log'. */
    private final Object accessMutex; // Protecting concurrent access to the log files
    /**Mutex to control the writes in the 'errorLog.log'. */
    private final Object errorMutex;
    /**Socket through which we will send the response.*/
    private final Socket socket;
    /**Long that identifies the current thread*/
    private final long threadId; //

    /**
     * @param s Socket through which we will send the response.
     * @param properties Different server properties specified in 'server.properties' such as the default file or
     *      * the default directory.
     * @param accessMutex Mutex to control the writes in the 'accessLog.log'.
     * @param errorMutex Mutex to control the writes in the 'errorLog.log'.
     * @param threadId Id of the current thread.
     */
    public HTTPResponse(Socket s, Properties properties, Object accessMutex, Object errorMutex, long threadId) {
        date.setTimeZone(TimeZone.getTimeZone("GMT+1")); // Setting time zone
        dateLog.setTimeZone(TimeZone.getTimeZone("GMT+1"));

        this.socket = s;
        this.threadId = threadId;
        this.accessMutex = accessMutex;
        this.errorMutex = errorMutex;
        this.basePath = properties.getProperty("BASE_DIRECTORY");
        this.defaultFile = new File (basePath + properties.getProperty("DEFAULT_FILE"));
        this.allow = Boolean.parseBoolean(properties.getProperty("ALLOW"));
    }

    /**
     * Main method which will handle the syntax related analysis of the request, delegating to other functions
     * all the rest.
     * @param request HTTP request line.
     * @param ifModSince String of the parameter If-Modified-Since; null if there's no one.
     * @throws Exception Different exceptions that can occur into all the sub-functions called by this one;
     * mainly I/O Exceptions.
     */
    public void response(String request, String ifModSince) throws Exception {
        if ( request == null ) {
            badRequest("null");
            return;
        }

        StringTokenizer tokenizer = new StringTokenizer(request);
        if ( tokenizer.countTokens() != 3 ) {
            badRequest(request);
            return;
        }
        String lMethod = tokenizer.nextToken();
        String lUrl = tokenizer.nextToken();
        String lVersion = tokenizer.nextToken();

        // HTML version analysis
        if ( !lVersion.equals("HTTP/1.0") && !lVersion.equals("HTTP/1.1") ) {
            badRequest(request);
            return;
        }

        // URl analysis
        if ( lUrl.contains(".do") ) { // Managing dynamic requests
            dynRequest(request, lUrl);
            return;
        }

        File file = new File(basePath + lUrl);
        if ( file.isDirectory() ) { // Managing directories
            if ( new File(file.getPath() + "/" + defaultFile.getName()).exists() )
                file = defaultFile;
            else {
                if ( allow )
                    allowRequest(request, file); // Managing allow requests
                else
                    accessForbidden(request);
                return;
            }
        } else if ( !file.exists() ) { // Testing that file exists
            notFound(request);
            return;
        } else if ( !file.canRead() ) {
            accessForbidden(request);
            return;
        }

        // Method analysis
        if ( lMethod.equals("GET") ) {
            if ( ifModSince == null ) // There's no if-Modified-Since parameter
                requestOK(request, file);
            else
                modifiedSince(request, ifModSince, file);

        } else if ( lMethod.equals("HEAD") ) {
            sendResponse("HTTP/1.0 200 OK\n" + getHeaders(file), null);
            addAccessToLog(request, "HTTP/1.0 200 OK", file);  

        } else
            badRequest(request);
    }

    /**
     * Method in charge of sending to the connected client to the socket, the file and the headers passed as an argument.
     * @param headers All the headers to send.
     * @param file File to send; null if no file are going to be sent.
     * @throws IOException If an I/O error occurs when sending the response.
     */
    private void sendResponse(String headers, File file) throws IOException {
        int length;
        BufferedInputStream fileInput;
        BufferedOutputStream socketOutput = new BufferedOutputStream(socket.getOutputStream());
        byte[] buff = new byte[1024];

        socketOutput.write(headers.getBytes());
        if ( file != null ) {
            fileInput = new BufferedInputStream(new FileInputStream(file));
            while ( (length = fileInput.read(buff)) > 0 )
                socketOutput.write(buff, 0, length);
            fileInput.close();
        }
        socketOutput.flush();
        socketOutput.close();
    }

    /**
     * Get all the headers (date, server, last modified, content-type and content-length)
     * for the file passed as an argument
     * @param file The file which get the headers.
     * @return String with the headers.
     * @throws IOException If and I/O error occurs when generating the headers.
     */
    private String getHeaders(File file) throws IOException {
        return "Date: " + date.format(new Date()) + "\n" +
                "Server: WebServer_555" + "\n" +
                "Last-Modified: " + date.format(file.lastModified()) + "\n" +
                "Content-Length: " + Files.size(file.toPath()) + "\n" +
                "Content-Type: " + Files.probeContentType(file.toPath()) + "\n\n";
    }

    /**
     * Add a correct request to the AccessLog.
     * @param request HTTP request.
     * @param accessMessage Message sent to the client.
     * @param content File sent to the client.
     * @throws IOException If an I/O error occurs when writing to the log.
     */
    private void addAccessToLog(String request, String accessMessage, File content) throws IOException {
        synchronized (accessMutex) {
            PrintWriter logOutput = new PrintWriter(new FileOutputStream(accessLog, true), true);
            logOutput.printf("----------" +
                    "\nRequest: " + request +
                    "\nAddress: " + socket.getInetAddress() + ":" + socket.getPort() +
                    "\nDate: " + dateLog.format(new Date()) +
                    "\nResponse: " + accessMessage +
                    "\nContent-Length: " + Files.size(content.toPath()) + "\n----------");
        }
    }

    /**
     * Add an incorrect request to the ErrorLog.
     * @param request HTTP request.
     * @param errorMessage Message sent to the client.
     * @throws FileNotFoundException If the ErrorLog couldn't be found.
     */
    private void addErrorToLog(String request, String errorMessage) throws FileNotFoundException {
        synchronized (errorMutex) {
            PrintWriter logOutput = new PrintWriter(new FileOutputStream(errorLog, true), true);
            logOutput.printf("----------" +
                    "\nRequest: " + request +
                    "\nAddress: " + socket.getInetAddress() + ":" + socket.getPort() +
                    "\nDate: " + dateLog.format(new Date()) +
                    "\nResponse: " + errorMessage + "\n----------");
        }
    }

    /**
     * Method to handle the Bad requests.
     * @param request HTTP request.
     * @throws IOException If an I/O error occurs.
     */
    private void badRequest(String request) throws IOException {
        sendResponse("HTTP/1.0 400 Bad Request\n" + getHeaders(error400), error400);
        addErrorToLog(request, "HTTP/1.0 400 Bad Request");
    }

    /**
     * Method to handle the Access Forbidden requests.
     * @param request HTTP request.
     * @throws IOException If an I/O error occurs.
     */
    private void accessForbidden(String request) throws IOException {
        sendResponse("HTTP/1.0 403 Access Forbidden\n" + getHeaders(error403), error403);
        addErrorToLog(request, "HTTP/1.0 403 Access Forbidden");
    }

    /**
     * Method to handle the Not Found requests.
     * @param request HTTP request.
     * @throws IOException If an I/O error occurs.
     */
    private void notFound(String request) throws IOException {
        sendResponse("HTTP/1.0 404 Not Found\n" + getHeaders(error404), error404);
        addErrorToLog(request, "HTTP/1.0 404 Not Found");
    }

    /**
     * Method to handle the Not Modified requests.
     * @param request HTTP request.
     * @param file The not-modified file.
     * @throws IOException If an I/O error occurs.
     */
    private void notModified(String request, File file) throws IOException {
        sendResponse("HTTP/1.0 304 Not Modified\n" + "Date: " + date.format(new Date()) + "\n" +
                "Server: WebServer_555" + "\n\n", null);
        addAccessToLog(request, "HTTP/1.0 304 Not Modified", file);
    }

    /**
     * Method to handle the OK requests.
     * @param request HTTP request.
     * @param file The file requested.
     * @throws IOException If an I/O error occurs.
     */
    private void requestOK(String request, File file) throws IOException {
        sendResponse("HTTP/1.0 200 OK\n" + getHeaders(file), file);
        addAccessToLog(request, "HTTP/1.0 200 OK", file);
    }

    /**
     * Method to test if the file was modified since the specified date; it will also send to the client the correspond
     * message based on that comparison.
     * @param request HTTP request.
     * @param ifModSince The If-Modified-Since header parameter.
     * @param file File requested.
     * @throws IOException If an I/0 error occurs while comparing the dates.
     */
    private void modifiedSince(String request, String ifModSince, File file) throws IOException {
        try {
            Date modSince = date.parse(ifModSince);
            Date lastMod = date.parse(date.format(file.lastModified()));
            if ( modSince.compareTo(lastMod) >= 0 )
                notModified(request, file);
            else
                requestOK(request, file);

        } catch (ParseException | IOException e) { // Incorrect data format
            badRequest(request);
        }
    }

    /**
     * Method to process the requests to show a list of the files inside a directory.
     * @param request HTTP request.
     * @param dir Directory where to look into.
     * @throws IOException If an I/O exception occurs.
     */
    private void allowRequest(String request, File dir) throws IOException {
        StringBuilder htmlFile;
        File[] files;

        // Creating the html text
        if ( (files = dir.listFiles()) == null ) {
            badRequest(request);
            return;
        }

        htmlFile = new StringBuilder("<html> <body> <h1>");
        for ( File file : files ) {
            htmlFile.append("<h1><a href=\"").
                    append(file.toPath().toString().replaceFirst(basePath, "")).
                    append("\">").append(file.getName()).append("</a></h1>");
        }
        htmlFile.append("</h1></body></html>");

        // Creating the file
        File tmpAllowFile = null;
        try {
            tmpAllowFile = new File(basePath + "tempAllowFile" + threadId);
            Files.writeString(tmpAllowFile.toPath(), htmlFile);
            sendResponse("HTTP/1.0 200 OK\n" + "Date: " + date.format(new Date()) + "\n" +
                    "Server: WebServer_555\n\n", tmpAllowFile);
            addAccessToLog(request, "HTTP/1.0 200 OK", tmpAllowFile);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            tmpAllowFile.delete(); // Delete the file
        }
    }

    /**
     * Method to handle the dynamic requests made by the client.
     * @param request HTTP request.
     * @param lUrl Complete URL.
     * @throws Exception If an error occurs while generating the dynamic page.
     */
    private void dynRequest(String request, String lUrl) throws Exception {
        Map<String, String> properties = new HashMap<>();
        int i = 0;
        String servletClass;

        // Parsing class
        if ( lUrl.contains(MyServlet.class.getSimpleName() + ".do") )
            servletClass = MyServlet.class.getSimpleName();
        else if ( lUrl.contains(YourServlet.class.getSimpleName() + ".do") )
            servletClass = YourServlet.class.getSimpleName();
        else {
            badRequest(request);
            return;
        }

        // Getting all parameters
        int start = lUrl.indexOf('?');
        String[] args = lUrl.substring(start+1).split("&");

        while ( i < args.length ) {
            String[] keyValue = args[i].split("=");
            if ( keyValue.length == 2 )
                properties.put(keyValue[0], keyValue[1].replace("+", " "));
            else {
                properties.put(keyValue[0], "");
            }
            i++;
        }

        // Creating a temporal file to let us use the sendResponse function
        File tmpDynFile = null;
        try {
            tmpDynFile = new File(basePath + "tempDynPage" + threadId);
            Files.writeString(tmpDynFile.toPath(), ServerUtils.processDynRequest(servletClass, properties));

            sendResponse("HTTP/1.0 200 OK\n" + "Date: " + date.format(new Date()) + "\n" +
                    "Server: WebServer_555\n\n", tmpDynFile);
            addAccessToLog(request, "HTTP/1.0 200 OK", tmpDynFile);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            tmpDynFile.delete();
        }
    }
}
