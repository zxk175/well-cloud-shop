package com.zxk175.well.provider.config;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.common.collect.Lists;
import com.zxk175.well.base.consts.Const;
import com.zxk175.well.base.util.json.FastJsonValueFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

/**
 * fastJson配置
 *
 * @author zxk175
 * @since 2019-09-02 09:42
 */
@Configuration
public class MyFastJsonConfig {

    @Bean
    public StringHttpMessageConverter stringConverter() {
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(Const.UTF_8_OBJ);

        List<MediaType> mediaTypes = Lists.newArrayList();
        mediaTypes.add(MediaType.TEXT_HTML);
        mediaTypes.add(MediaType.TEXT_PLAIN);
        stringConverter.setSupportedMediaTypes(mediaTypes);

        return stringConverter;
    }

    @Bean
    public FastJsonHttpMessageConverter fastJsonConverter() {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter() {
            @Override
            protected void writeInternal(Object data, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
                if (data instanceof String) {
                    Charset charset = this.getContentTypeCharset(Objects.requireNonNull(outputMessage.getHeaders().getContentType()));
                    StreamUtils.copy(Convert.toStr(data), charset, outputMessage.getBody());
                } else {
                    super.writeInternal(data, outputMessage);
                }
            }

            private Charset getContentTypeCharset(MediaType contentType) {
                Charset charset = contentType.getCharset();
                if (ObjectUtil.isNotNull(contentType) && ObjectUtil.isNotNull(charset)) {
                    return charset;
                }

                return this.getDefaultCharset();
            }
        };

        // 消息转换器默认编码
        fastConverter.setDefaultCharset(Const.UTF_8_OBJ);

        // 这里顺序不能反，一定先写text/html,不然ie下出现下载提示
        List<MediaType> mediaTypes = Lists.newArrayList();
        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastConverter.setSupportedMediaTypes(mediaTypes);

        FastJsonConfig fastJsonConfig = new FastJsonConfig();

        // 默认编码
        fastJsonConfig.setCharset(Const.UTF_8_OBJ);

        // 日期格式化
        // 注释该配置 可以读取日期@JSONField(format = "")
        JSON.DEFFAULT_DATE_FORMAT = Const.DATE_TIME_FORMAT_DEFAULT;

        // 序列化规则
        fastJsonConfig.setSerializerFeatures(Const.serializerFeatures());

        // 序列化过滤器
        SerializeFilter[] serializeFilters = {new FastJsonValueFilter()};
        fastJsonConfig.setSerializeFilters(serializeFilters);

        // 序列化配置
        SerializeConfig config = Const.serializeConfig();
        fastJsonConfig.setSerializeConfig(config);

        fastConverter.setFastJsonConfig(fastJsonConfig);

        return fastConverter;
    }
}
