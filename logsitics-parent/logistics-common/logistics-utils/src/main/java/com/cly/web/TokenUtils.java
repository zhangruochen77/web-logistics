package com.cly.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * 生成 jwt token 的工具类
 */
public class TokenUtils {

    /**
     * 加密密钥
     */
    private static final String SECRET = "qadjjwhxcndba";

    /**
     * 过期时间 30 分钟
     */
    private static final Long EXPERT_TIME = 1800000L;

    /**
     * 通过 id 和 username 共同生成 token
     *
     * @param id
     * @param username
     * @return
     */
    public static String createToken(Long id, String username) {

        // 设置过期时间
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.plus(EXPERT_TIME, ChronoUnit.MILLIS);
        ZonedDateTime zone = localDateTime.atZone(zoneId);
        Date expertTime = Date.from(zone.toInstant());

        // 生成 token
        String token = JWT.create()
                .withClaim("id", id)
                .withClaim("username", username)
                .withExpiresAt(expertTime)
                .sign(Algorithm.HMAC256(SECRET));

        return token;
    }

    /**
     * 通过 token 获取用户的 id
     *
     * @param token
     * @return
     */
    public static Long getId(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET))
                .build()
                .verify(token)
                .getClaim("id")
                .asLong();
    }

    /**
     * 通过 token 获取用户账户名称
     *
     * @param token
     * @return
     */
    public static String getUsername(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET))
                .build()
                .verify(token)
                .getClaim("username")
                .asString();
    }

    /**
     * 获取 token 的过期时间
     *
     * @param token
     * @return
     */
    public static Date getExpertTime(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET))
                .build()
                .verify(token)
                .getExpiresAt();
    }

    public static void main(String[] args) {
        System.out.println(TokenUtils.getId("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MTU3MjUxMDgyMzg0NTI4MTc5MywiZXhwIjoxNjYzNzUyNTcwLCJ1c2VybmFtZSI6ImphY2sifQ.bgZ-K3d_6bjrBsvO_mM0tLLpiEQzV_5IYTfnNOI_Sw4"));
    }

}
