package com.github.mateusjose98.core;

import com.github.mateusjose98.util.WebConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;


public class WebServer {

    public static final Logger LOGGER = Logger.getLogger(WebServer.class.getName());

    private int portNumber;
    private ServerSocket serverSocket;
    private String httpMethod;
    private String resourcePath;

    public WebServer(int portNumber){
        try {
            this.portNumber = portNumber;
            this.serverSocket = new ServerSocket(portNumber);
        } catch (Exception e) {
            LOGGER.severe("Could not initialize server at port: " + portNumber);
        }
        start();

    }

    public void start() {
        LOGGER.info("Server started at port: " + portNumber);

        while (true) {
            Socket newSocket = null;
            try {
                newSocket = serverSocket.accept();
                handleRequest(newSocket);
            } catch (Exception e) {
                LOGGER.severe("Could not accept client connection");

            }
        }
    }

    private void handleRequest(Socket newSocket) {
        LOGGER.info("Handling request from client: " + newSocket.getInetAddress());
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(newSocket.getInputStream()));
            String line;
            do {
                line = bufferedReader.readLine();
                if (line == null || line.isEmpty()) {
                    break;
                }
                handleLineBuffer(newSocket, line);

            } while (!line.isBlank());
            newSocket.close();
        } catch (IOException e) {
            LOGGER.severe("Could not close client connection");

        }

    }

    private void handleLineBuffer(Socket newSocket, String line) {
        if(line.startsWith("GET") || line.startsWith("POST")) {
            String[] request = line.split(" ");
            httpMethod = request[0];
            resourcePath = request[1];
            System.out.println("HTTP Method: " + httpMethod + " At: " + resourcePath);

            String completePath = WebConfig.WEB_ROOT + resourcePath;
            System.out.println("Complete Path: " + completePath);

            try {
                OutputStream outputStream = newSocket.getOutputStream();
                if (Files.exists(Path.of(completePath)) && Files.isRegularFile(Path.of(completePath))) {
                    byte[] content = Files.readAllBytes(Path.of(completePath));
                    outputStream.write("HTTP/1.1 200 OK\r\n".getBytes());
                    outputStream.write(("Content-Type: " + WebConfig.MIME_TYPES.get(completePath.substring(completePath.lastIndexOf(".") + 1) + "\r\n")).getBytes());
                    outputStream.write(("Content-Length: " + content.length + "\r\n").getBytes());
                    outputStream.write("\r\n".getBytes());
                    outputStream.write(content);
                } else {
                    outputStream.write("HTTP/1.1 404 Not Found\r\n".getBytes());
                    outputStream.write("Content-Type: text/html\r\n".getBytes());
                    outputStream.write("\r\n".getBytes());
                    outputStream.write("<h1>404 Not Found</h1>".getBytes());
                }

            } catch (IOException e) {
                LOGGER.severe("Could not write to client connection");
            }


        }

    }

    public WebServer() throws IOException {
        this(8080);
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }
}
