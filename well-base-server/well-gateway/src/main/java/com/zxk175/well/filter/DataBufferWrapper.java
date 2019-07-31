package com.zxk175.well.filter;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;

/**
 * @author zxk175
 * @since 2019/07/10 20:04
 */
public class DataBufferWrapper {

    private byte[] data;
    private DataBufferFactory factory;

    public DataBufferWrapper(byte[] data, DataBufferFactory factory) {
        this.data = data;
        this.factory = factory;
    }

    public byte[] getData() {
        return data;
    }

    public DataBufferFactory getFactory() {
        return factory;
    }

    public DataBuffer newDataBuffer() {
        if (factory == null) {
            return null;
        }

        return factory.wrap(data);
    }
}
