package com.zxk175.well.common.error;

import com.zxk175.well.common.http.Response;
import com.zxk175.well.common.util.net.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * @author zxk175
 * @since 2019/02/28 10:48
 */
@Slf4j
@Controller
@RequestMapping("/error")
public class MyErrorController extends AbstractErrorController {

    private final ErrorProperties errorProperties;


    @Autowired
    public MyErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties) {
        super(errorAttributes);
        this.errorProperties = serverProperties.getError();
    }

    @Override
    public String getErrorPath() {
        return this.errorProperties.getPath();
    }

    @Override
    protected Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(request, includeStackTrace);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        errorAttributes.put("timestamp", dtf.format(LocalDateTime.now()));
        return errorAttributes;
    }

    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView pageError(HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(getStatus(request).value());

        // 自定义视图
        Map<String, Object> model = getErrorAttributes(request, true);
        return new ModelAndView("error", model);
    }

    @ResponseBody
    @RequestMapping
    public Object apiError(HttpServletRequest request) {
        HttpStatus httpStatus = getStatus(request);

        String msg;
        int code = httpStatus.value();
        switch (code) {
            case 404:
                String errorUri = RequestUtil.requestUrl(request);
                msg = "请求地址不存在：" + errorUri;
                break;
            case 500:
                msg = "服务器内部错误";
                break;
            default:
                msg = "请求错误";
                break;
        }

        log.error(msg);
        return Response.fail(code, msg);
    }
}