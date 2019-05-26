package com.zxk175.well.config.filter.request;

import cn.hutool.core.util.ObjectUtil;
import com.zxk175.well.common.consts.Const;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * @author zxk175
 * @since 2018/3/30 09:31
 */
@Slf4j
public class RepeatedlyReadWrapper extends HttpServletRequestWrapper {

    private static final int BUFFER_START_POSITION = 0;
    private static final int BUFFER_LENGTH = 1024;
    private final String body;


    RepeatedlyReadWrapper(HttpServletRequest request) throws IOException {
        super(request);

        StringBuilder bufferStr = new StringBuilder(BUFFER_LENGTH);
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = request.getInputStream();
            if (ObjectUtil.isNotNull(inputStream)) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Const.UTF_8_OBJ));

                char[] charBuffer = new char[BUFFER_LENGTH];
                int bytesRead;
                while ((bytesRead = bufferedReader.read(charBuffer, 0, BUFFER_LENGTH)) > 0) {
                    bufferStr.append(charBuffer, BUFFER_START_POSITION, bytesRead);
                }
            }
        } catch (IOException e) {
            log.error("Error reading the request body…", e);
        } finally {
            if (ObjectUtil.isNotNull(bufferedReader)) {
                bufferedReader.close();
            }
        }

        body = bufferStr.toString();
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes(Const.UTF_8_OBJ));
        return new MyServletInputStream(byteArrayInputStream);
    }
}
