package com.hang.manage.system.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


import javax.servlet.ServletException;
import java.util.Date;

public class JwtUtil {
    /**
     * 私钥
     */
    final static String base64EncodedSecretKey = "base64EncodedSecretKey";
    /**
     * 过期时间,使用20分钟
     */
    final static long TOKEN_EXP = 1000 * 60 * 20;

    //产生一个token
    public static String CreatToken(String userName) {
        return Jwts.builder()
                .setSubject(userName)
                .claim("roles", "user")
                .setIssuedAt(new Date()) //当前时间
                /*过期时间*/
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXP))
                .signWith(SignatureAlgorithm.HS256, base64EncodedSecretKey)
                .compact();
    }



    /**
     * @func 检查token, 只要不正确就会抛出异常
     */
    static void checkToken(String token) throws ServletException {
        try {
            final Claims claims = Jwts.parser().setSigningKey(base64EncodedSecretKey).parseClaimsJws(token).getBody();
            } catch (ExpiredJwtException e1) {
            throw new ServletException("token expired");
        } catch (Exception e) {
            throw new ServletException("other token exception");
        }
    }

    /**
     * @func token ok返回true
     */
    public static boolean isTokenOk(String token) {
        try {
            Jwts.parser().setSigningKey(base64EncodedSecretKey).parseClaimsJws(token).getBody();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 根据token返回 对象
     */
    public static String getUsername(String token) {
            Claims body = Jwts.parser().setSigningKey(base64EncodedSecretKey).parseClaimsJws(token).getBody();
        String subject = body.getSubject();
        System.out.println("用户->"+subject);
        return subject;
    }

    /**
     * @func token ok返回true
     */
    public static void invalidate(String token) {
        Claims body = Jwts.parser().setSigningKey(base64EncodedSecretKey).parseClaimsJws(token).getBody().setExpiration(new Date());
    }

    /**
     * 拆出key
     * @param sub
     * @param key
     * @return
     */
    public static String getTokenByKey(String sub,String key){
        return key.substring(sub.length());
    }

    /**
     * 生成key
     * @param sub
     * @param key
     * @return
     */
    public static String generateTokenByKey(String sub,String key){
        return sub+key;
    }


}
