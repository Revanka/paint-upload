package com.github.elopteryx.upload.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;

class ByteBufferBackedInputStreamTest {

    private static final String TEST_TEXT = "Test text.";

    @Test
    void create_with_direct() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> {
            new ByteBufferBackedInputStream(ByteBuffer.allocate(0).asReadOnlyBuffer());
        });
    }

    @Test
    void create_with_read_only() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> {
            new ByteBufferBackedInputStream(ByteBuffer.allocateDirect(0));
        });
    }

    @Test
    void check_available_bytes() throws Exception {
        ByteBufferBackedInputStream stream = new ByteBufferBackedInputStream(ByteBuffer.wrap(TEST_TEXT.getBytes()));
        int length = TEST_TEXT.getBytes().length;
        assertEquals(length, stream.available());
        int read = stream.read(new byte[1024]);
        assertEquals(length, read);
        assertEquals(0, stream.available());
    }

    @Test
    void read_a_byte() throws Exception {
        ByteBufferBackedInputStream stream = new ByteBufferBackedInputStream(ByteBuffer.wrap(TEST_TEXT.getBytes()));
        int read = stream.read();
        assertEquals(read, "T".getBytes()[0]);
    }

    @Test
    void try_to_read_an_exhausted_stream() throws Exception {
        ByteBufferBackedInputStream stream = new ByteBufferBackedInputStream(ByteBuffer.allocate(0));
        int read = stream.read();
        assertEquals(read, -1);
    }

    @Test
    void try_to_read_a_closed_stream() throws Exception {
        ByteBufferBackedInputStream stream = new ByteBufferBackedInputStream(ByteBuffer.wrap(TEST_TEXT.getBytes()));
        stream.close();
        assertThrows(IOException.class, stream::read);
    }

    @Test
    void read_into_byte_array() throws Exception {
        ByteBufferBackedInputStream stream = new ByteBufferBackedInputStream(ByteBuffer.wrap(TEST_TEXT.getBytes()));
        byte[] buf = new byte[1024];
        int read = stream.read(buf);
        assertEquals(TEST_TEXT, new String(buf, 0, read));
    }

    @Test
    void try_to_read_an_exhausted_stream_to_array() throws Exception {
        ByteBufferBackedInputStream stream = new ByteBufferBackedInputStream(ByteBuffer.allocate(0));
        byte[] buf = new byte[1024];
        int read = stream.read(buf);
        assertEquals(read, -1);
    }

    @Test
    void try_to_read_a_closed_stream_to_array() throws Exception {
        ByteBufferBackedInputStream stream = new ByteBufferBackedInputStream(ByteBuffer.wrap(TEST_TEXT.getBytes()));
        stream.close();
        byte[] buf = new byte[1024];
        assertThrows(IOException.class, () -> stream.read(buf));
    }

}