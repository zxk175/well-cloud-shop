package com.zxk175.well.common.util.net;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.zxk175.well.common.consts.Const;
import com.zxk175.well.common.util.InCacheUtil;
import com.zxk175.well.common.util.Md5Util;
import com.zxk175.well.common.util.MyStrUtil;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.client.ResourceAccessException;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * Ip工具类
 *
 * @author zxk175
 * @since 17/11/1 14:31
 */
public class IpUtil {

    public static String getClientIp(HttpServletRequest request) {
        String unknown = "unknown";
        if (ObjectUtil.isNull(request)) {
            return "unknown";
        }

        String ip = request.getHeader("x-forwarded-for");
        boolean flag = MyStrUtil.isBlank(ip);

        if (flag || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (flag || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (flag || unknown.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (Const.IPV6_LOCAL.equals(ip)) {
            ip = "127.0.0.1";
        }
        if (ip.split(MyStrUtil.COMMA).length > 1) {
            ip = ip.split(StrUtil.COMMA)[0];
        }
        return ip;
    }

    public static String getAddressByIp(String ip) {
        String result;

        try {
            String fileClassPath = "ip/ip2region.db";
            ClassPathResource classPathResource = new ClassPathResource(fileClassPath);
            InCacheUtil cacheUtil = new InCacheUtil(classPathResource.getInputStream());
            InputStream cacheIn = cacheUtil.getInputStream();

            String tmpDir = System.getProperty("java.io.tmpdir");
            String fullPath = tmpDir + fileClassPath;
            File dbFile = new File(fullPath);
            boolean existsFlag = dbFile.exists();
            boolean md5Flag = true;

            // 判断Md5是否一致
            if (existsFlag) {
                String md5One = Md5Util.md5(new FileInputStream(dbFile));
                String md5Two = Md5Util.md5(cacheIn);
                md5Flag = MyStrUtil.ne(md5One, md5Two);

                if (md5Flag) {
                    FileUtil.del(dbFile);
                }
            }

            // 判断文件是否存在
            existsFlag = dbFile.exists();
            if (existsFlag || md5Flag) {
                dbFile = FileUtil.writeFromStream(cacheUtil.getInputStream(), fullPath);
            }

            result = getIpRegion(ip, dbFile.getAbsolutePath());
        } catch (ResourceAccessException | SocketTimeoutException | SocketException e) {
            result = "请求超时或请求关闭";
        } catch (Exception e) {
            result = "未知错误 " + e.toString();
        }

        return result;
    }

    private static String getIpRegion(String ip, String dbPath) throws Exception {
        if (Util.isIpAddress(ip)) {
            DbConfig config = new DbConfig();
            DbSearcher searcher = new DbSearcher(config, dbPath);
            DataBlock dataBlock = searcher.btreeSearch(ip);
            searcher.close();
            return dataBlock.getRegion();
        }

        throw new RuntimeException("Error: Invalid IP Address");
    }
}
