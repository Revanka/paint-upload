package com.elopteryx.paint.upload.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

class MockServletInputStream extends ServletInputStream {

    private static final String fileName = "foo.txt";
    private static final String requestData =
            "-----1234\r\n"
                    + "Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"\r\n"
                    + "Content-Type: text/whatever\r\n"
                    + "\r\n"
                    + "This is the content of the file\n"
                    + "\r\n"
                    + "-----1234\r\n"
                    + "Content-Disposition: form-data; name=\"field\"\r\n"
                    + "\r\n"
                    + "fieldValue\r\n"
                    + "-----1234\r\n"
                    + "Content-Disposition: form-data; name=\"multi\"\r\n"
                    + "\r\n"
                    + "value1\r\n"
                    + "-----1234\r\n"
                    + "Content-Disposition: form-data; name=\"multi\"\r\n"
                    + "\r\n"
                    + "value2\r\n"
                    + "-----1234--\r\n";
    
    private ByteArrayInputStream sourceStream;

    private boolean ready;

    private boolean finished;
    
    private ReadListener readListener;


    public MockServletInputStream() {
        this.sourceStream = new ByteArrayInputStream(requestData.getBytes(StandardCharsets.US_ASCII));
    }

    public MockServletInputStream(String data) {
        this.sourceStream = new ByteArrayInputStream(data.getBytes(StandardCharsets.US_ASCII));
    }


    @Override
    public int read() throws IOException {
        return this.sourceStream.read();
    }

    @Override
    public void close() throws IOException {
        super.close();
        this.sourceStream.close();
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public boolean isReady() {
        return ready;
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        this.readListener = readListener;
    }
}