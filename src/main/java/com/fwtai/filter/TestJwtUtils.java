package com.fwtai.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestJwtUtils{

    public static final String TOKEN_HEADER = "Authorization";

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String SUBJECT = "congge";

    public static final long EXPIRITION = 1000 * 24 * 60 * 60 * 7;

    public static final String APPSECRET_KEY = "congge_secret";

    private static final String ROLE_CLAIMS = "rol";

    /**
     * 生成token
     * @param username
     * @param roles
     * @return
     */
    public static String createToken(final String username,final ArrayList<String> roles){
        final Map<String,Object> map = new HashMap<>();
        map.put(ROLE_CLAIMS,roles);
        return Jwts.builder().setSubject(username).setClaims(map).claim("username",username).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + EXPIRITION)).signWith(SignatureAlgorithm.HS256,APPSECRET_KEY).compact();
    }

    public static Claims checkJWT(String token){
        try{
            final Claims claims = Jwts.parser().setSigningKey(APPSECRET_KEY).parseClaimsJws(token).getBody();
            return claims;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取用户名
     * @param token
     * @return
     */
    public static String getUsername(final String token){
        final Claims claims = Jwts.parser().setSigningKey(APPSECRET_KEY).parseClaimsJws(token).getBody();
        return claims.get("username").toString();
    }

    /**
     * 获取用户角色
     * @param token
     * @return
     */
    public static List<String> getUserRole(final String token){
        final Claims claims = Jwts.parser().setSigningKey(APPSECRET_KEY).parseClaimsJws(token).getBody();
        return claims.get(ROLE_CLAIMS,List.class);
    }

    /**
     * 是否过期
     * @param token
     * @return
     */
    public static boolean isExpiration(String token){
        Claims claims = Jwts.parser().setSigningKey(APPSECRET_KEY).parseClaimsJws(token).getBody();
        return claims.getExpiration().before(new Date());
    }
}