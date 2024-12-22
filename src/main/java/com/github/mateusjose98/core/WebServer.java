package com.github.mateusjose98.core;

import com.github.mateusjose98.http.MyRequest;
import com.github.mateusjose98.http.MyResponse;
import com.github.mateusjose98.util.HttpMethod;
import com.github.mateusjose98.util.WebConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;


public class WebServer {

    public static final Logger LOGGER = LoggerFactory.getLogger(WebServer.class.getName());

    private int portNumber;
    private ServerSocket serverSocket;

    public WebServer(int portNumber){
        try {
            this.portNumber = portNumber;
            this.serverSocket = new ServerSocket(portNumber);
        } catch (Exception e) {
            LOGGER.error("Could not initialize server at port: " + portNumber);
        }
        start();

    }

    public void start() {
        LOGGER.info("Server started at port: " + portNumber);

        while (true) {
            Socket newSocket = null;
            try {
                newSocket = serverSocket.accept();
                handleConnection(newSocket);
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("Could not accept client connection");

            }
        }
    }

    private void handleConnection(Socket newSocket) {

        LOGGER.info("Handling request from client: " + newSocket.getInetAddress());
        MyRequest myRequest = new MyRequest();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(newSocket.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;

            do {
                line = bufferedReader.readLine();
                if (line == null || line.isEmpty()) {
                    break;
                }
                myRequest = handleLineBuffer(line, myRequest);

            } while (!line.isBlank());

            myRequest.setBody(getBodyIfExists(bufferedReader, myRequest));
            LOGGER.info("Request object created successfully: " + myRequest);


            writeResponse(newSocket, myRequest);

            newSocket.close();
        } catch (IOException e) {
            LOGGER.error("Could not close client connection");

        }

    }

    private String getBodyIfExists(BufferedReader bufferedReader, MyRequest myRequest) {
        try {
            if (myRequest.getHeaders().containsKey("content-length")) {
                int contentLength = Integer.parseInt(myRequest.getHeaders().get("content-length"));
                char[] body = new char[contentLength];
                bufferedReader.read(body, 0, contentLength);
                return new String(body);
            }
        } catch (IOException e) {
            LOGGER.error("Could not read body from client connection");
        }
        return null;
    }

    private MyRequest handleLineBuffer(String line, MyRequest myRequest) {
        if(line.startsWith(HttpMethod.GET.getMethod()) || line.startsWith(HttpMethod.POST.getMethod()) || line.startsWith(HttpMethod.PUT.getMethod()) || line.startsWith(HttpMethod.DELETE.getMethod()) || line.startsWith(HttpMethod.PATCH.getMethod()) || line.startsWith(HttpMethod.OPTIONS.getMethod())) {
            String[] request = line.split(" ");
            myRequest = new MyRequest(
                    null,
                    HttpMethod.valueOf(request[0]),
                    request[1]);

        } else {
            System.out.println(line.split(": ")[0] + " == " + line.split(": ")[1]);
            myRequest.getHeaders().put(line.split(": ")[0].toLowerCase(), line.split(": ")[1].toLowerCase());
        }
        return myRequest;

    }

    private static void writeResponse(Socket newSocket, MyRequest myRequest) {

        String completePath = new File(WebConfig.WEB_ROOT).getAbsoluteFile().getPath() + myRequest.getResourcePath();

        try {
            OutputStream outputStream = newSocket.getOutputStream();
            MyResponse res = new MyResponse(outputStream);
            if (Files.exists(Path.of(completePath)) && Files.isRegularFile(Path.of(completePath))) {
                byte[] fileBytes = Files.readAllBytes(Path.of(completePath));
                res.write("HTTP/1.1 200 OK\r\n");
                res.write("Content-Type: " + WebConfig.MIME_TYPES.get(completePath.substring(completePath.lastIndexOf(".") + 1)) + "\r\n");
                res.write("Content-Length: " + fileBytes.length + "\r\n");
                res.write("\r\n");
                outputStream.write(fileBytes);

            } else {
                if (WebConfig.ROUTES.get(myRequest.getResourcePath()) != null) {
                    Class<?> klass = WebConfig.ROUTES.get(myRequest.getResourcePath());

                    for (var method : klass.getDeclaredMethods()) {

                        if ( "void".equalsIgnoreCase(method.getReturnType().getName())) {
                            try {
                                method.invoke(klass.getDeclaredConstructor().newInstance());
                            } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    res.write("HTTP/1.1 200 OK\r\n");


                } else {
                    outputStream.write("HTTP/1.1 404 Not Found\r\n".getBytes());
                    outputStream.write("Content-Type: text/html\r\n".getBytes());
                    outputStream.write("\r\n".getBytes());
                    outputStream.write("<h1>404 Não encontramos a sua página/recurso!</h1>".getBytes());
                }
            }

        } catch (IOException e) {
            LOGGER.error("Could not write to client connection");
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

}
