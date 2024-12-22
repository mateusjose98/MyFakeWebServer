package com.github.mateusjose98.http;

import java.io.OutputStream;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class MyResponse {

    private String status;
    private String contentType;
    private LocalDateTime date;
    private static final String END_OF_LINE = "\r\n";
    private OutputStream outputStream;
    public MyResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.date = LocalDateTime.now();
    }

    public void write(String line) {
        try {
            outputStream.write((line).getBytes());
        } catch (Exception e) {
            e.printStackTrace();
    }


}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void send(Path path) {
        try {
            outputStream.write(END_OF_LINE.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
