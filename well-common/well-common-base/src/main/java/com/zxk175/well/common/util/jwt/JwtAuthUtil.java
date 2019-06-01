package com.zxk175.well.common.util.jwt;

import com.zxk175.well.common.util.json.FastJsonUtil;
import com.zxk175.well.common.util.jwt.bean.SysSubjectDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

/**
 * @author zxk175
 * @since 2017/5/5
 */
public class JwtAuthUtil {

    public static SysSubjectDTO sysSubject(String token) {
        Jws<Claims> claimsJws = parserSysJwt(token);
        // 解析subject
        String subject = claimsJws.getBody().getSubject();

        return FastJsonUtil.toObject(subject, SysSubjectDTO.class);
    }

    public static Jws<Claims> parserSysJwt(String token) {
        JwtParser parser = Jwts.parser();
        parser.setSigningKey(JwTokenUtil.getKeyInstance());

        return parser.parseClaimsJws(token);
    }
}